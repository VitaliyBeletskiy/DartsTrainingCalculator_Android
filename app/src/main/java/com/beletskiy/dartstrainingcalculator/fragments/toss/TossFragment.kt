package com.beletskiy.dartstrainingcalculator.fragments.toss

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.beletskiy.dartstrainingcalculator.databinding.FragmentTossBinding
import com.beletskiy.dartstrainingcalculator.fragments.score.ScoreFragment
import com.beletskiy.dartstrainingcalculator.utils.observeInLifecycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.onEach

class TossFragment : Fragment() {

    private lateinit var binding: FragmentTossBinding
    private val tossViewModel: TossViewModel by lazy {
        ViewModelProvider(this).get(TossViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTossBinding.inflate(inflater)
        binding.tossViewModel = tossViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*// FIXME: KLUDGE!!! otherwise pressing Up button sends the previous new Toss value
        findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.remove<Toss>(ScoreFragment.NEW_TOSS)*/

        // receiving events from ViewModel
        tossViewModel.eventsFlow
            .onEach { event ->
                when (event) {
                    is TossViewModel.Event.NavigateToScoreScreen -> {
                        findNavController()
                            .previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(ScoreFragment.NEW_TOSS, event.newToss)
                        findNavController().navigateUp()
                    }
                    is TossViewModel.Event.ShowSnackBar -> {
                        // TODO: разные сообщения для разных ошибок?
                        val message = getString(event.stringId)
                        Snackbar.make(binding.buttonAdd, message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            .observeInLifecycle(viewLifecycleOwner)
    }

}