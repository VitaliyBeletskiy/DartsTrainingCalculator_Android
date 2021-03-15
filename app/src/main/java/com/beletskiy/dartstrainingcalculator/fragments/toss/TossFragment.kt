package com.beletskiy.dartstrainingcalculator.fragments.toss

import android.os.Bundle
import android.os.Parcel
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.databinding.FragmentTossBinding
import com.beletskiy.dartstrainingcalculator.utils.observeInLifecycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.onEach

class TossFragment : Fragment() {

    private lateinit var binding: FragmentTossBinding
    private lateinit var tossViewModel: TossViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTossBinding.inflate(inflater)
        tossViewModel = ViewModelProvider(this).get(TossViewModel::class.java)
        binding.tossViewModel = tossViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // receiving events from ViewModel
        tossViewModel.eventsFlow
            .onEach {
                when (it) {
                    TossViewModel.Event.NavigateToMainScreen -> {}
                    is TossViewModel.Event.ShowSnackBar -> {
                        // TODO: разные сообщения для разных ошибок?
                        val message = getString(it.stringId)
                        Snackbar.make(binding.buttonAdd, message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            .observeInLifecycle(viewLifecycleOwner)
    }
}