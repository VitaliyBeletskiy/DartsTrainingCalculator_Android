package com.beletskiy.dartstrainingcalculator.fragments.gamedetails

import android.os.Bundle
import android.view.*
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.databinding.FragmentGameDetailsBinding
import com.beletskiy.dartstrainingcalculator.fragments.history.HistoryViewModel
import com.beletskiy.dartstrainingcalculator.utils.convertLongToDateString
import com.beletskiy.dartstrainingcalculator.utils.convertSavedTossListToString

class GameDetailsFragment : Fragment() {

    private lateinit var binding: FragmentGameDetailsBinding
    private val historyViewModel: HistoryViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            activity,  // not 'this', as we use this ViewModel in two Fragments
            HistoryViewModel.Factory(activity.application)
        ).get(HistoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        binding = FragmentGameDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.historyViewModel = historyViewModel

        val gameDetailsAdapter = GameDetailedAdapter()
        binding.recyclerView.adapter = gameDetailsAdapter
//        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
//        binding.recyclerView.layoutManager = gridLayoutManager

        historyViewModel.selectedGameAndTosses.observe(viewLifecycleOwner){
            it?.let {
                gameDetailsAdapter.submitList(it.savedTossList)
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.game_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.share) {
            shareGame()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun shareGame() {
        val intent = ShareCompat.IntentBuilder.from(requireActivity())
            .setText(resources.getString(
                R.string.share_game_text,
                convertLongToDateString(historyViewModel.selectedGameAndTosses.value?.savedGame?.timestamp ?: 0L, requireContext()),
                this.historyViewModel.selectedGameAndTosses.value?.savedGame?.points ?: 0,
                historyViewModel.selectedGameAndTosses.value?.savedTossList?.size ?: 0,
                convertSavedTossListToString(historyViewModel.selectedGameAndTosses.value?.savedTossList ?: emptyList()),
            ))
            .setType("text/plain")
            .intent
        startActivity(intent)
    }

}