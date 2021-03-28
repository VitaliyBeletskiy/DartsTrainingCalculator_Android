package com.beletskiy.dartstrainingcalculator.fragments.gamedetails

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.databinding.FragmentGameDetailsBinding
import com.beletskiy.dartstrainingcalculator.fragments.history.HistoryViewModel
import com.beletskiy.dartstrainingcalculator.utils.TAG

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

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.game_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.share) {
            // TODO: add code for "Share"
            Log.i(TAG, "GameDetailsFragment: Share clicked ")
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

}