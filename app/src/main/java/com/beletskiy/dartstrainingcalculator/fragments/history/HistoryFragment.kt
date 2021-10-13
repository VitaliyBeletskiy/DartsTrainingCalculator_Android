package com.beletskiy.dartstrainingcalculator.fragments.history

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.data.GameAndTosses
import com.beletskiy.dartstrainingcalculator.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment(), HistoryAdapter.RowClickListener {

    private lateinit var binding: FragmentHistoryBinding
    private val historyViewModel: HistoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        binding = FragmentHistoryBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.historyViewModel = historyViewModel

        val historyAdapter = HistoryAdapter(this)
        binding.recyclerView.adapter = historyAdapter

        // to reverse items order in RecyclerView = latest on the top
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        linearLayoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = linearLayoutManager

        // observe changes in List<GameAndTosses>
        historyViewModel.gameAndTossesList.observe(viewLifecycleOwner) {
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

    // handles the situation when User clicked "Delete" from row's pop-up menu
    override fun onPopupMenuItemClicked(gameId: Long) {
        historyViewModel.deleteSavedGame(gameId)
    }

    // handles the situation when User clicked the entire row
    override fun onRowClicked(gameAndTosses: GameAndTosses) {
        historyViewModel.selectedGameAndTosses.value = gameAndTosses
        findNavController().navigate(HistoryFragmentDirections.actionHistoryToGameDetails())
    }

}