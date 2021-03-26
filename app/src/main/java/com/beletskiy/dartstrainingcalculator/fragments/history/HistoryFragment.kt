package com.beletskiy.dartstrainingcalculator.fragments.history

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.databinding.FragmentHistoryBinding
import com.beletskiy.dartstrainingcalculator.fragments.score.ScoreAdapter
import com.beletskiy.dartstrainingcalculator.utils.TAG

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val historyViewModel: HistoryViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            HistoryViewModel.Factory(activity.application)
        ).get(HistoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentHistoryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.historyViewModel = historyViewModel

        val historyAdapter = HistoryAdapter()
        binding.recyclerView.adapter = historyAdapter

        historyViewModel.savedGameList.observe(viewLifecycleOwner) {
            it?.let {
                historyAdapter.submitList(it)
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.clearHistory) {
            deleteAllDataWithConfirmation()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    /// asks if User is sure and wants to delete all data from history
    private fun deleteAllDataWithConfirmation() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(getString(R.string.confirm)) { _, _ ->
                    historyViewModel.deleteAllData()
                }
                setNegativeButton(getString(R.string.cancel)) { _, _ ->
                }
            }
            // Set other dialog properties
            builder.setMessage(getString(R.string.delete_all_data))
                ?.setTitle(getString(R.string.delete_all))

            // Create the AlertDialog
            builder.create()
        }
        alertDialog?.show()
    }

}