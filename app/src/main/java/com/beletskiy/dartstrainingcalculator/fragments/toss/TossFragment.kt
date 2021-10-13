package com.beletskiy.dartstrainingcalculator.fragments.toss

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.beletskiy.dartstrainingcalculator.databinding.FragmentTossBinding
import com.beletskiy.dartstrainingcalculator.fragments.score.ScoreViewModel
import com.beletskiy.dartstrainingcalculator.utils.observeInLifecycle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TossFragment : Fragment() {

    private lateinit var binding: FragmentTossBinding
    private val scoreViewModel: ScoreViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTossBinding.inflate(inflater)
        binding.vm = scoreViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // receiving events from ViewModel
        scoreViewModel.eventsFlow
            .onEach { event ->
                when (event) {
                    is ScoreViewModel.Event.NavigateBackToScoreScreen -> {
                        findNavController().navigateUp()
                    }
                    is ScoreViewModel.Event.ShowSnackBar -> {
                        val message = getString(event.stringId)
                        Snackbar.make(binding.buttonAdd, message, Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {
                    }
                }
            }
            .observeInLifecycle(viewLifecycleOwner)
    }

}