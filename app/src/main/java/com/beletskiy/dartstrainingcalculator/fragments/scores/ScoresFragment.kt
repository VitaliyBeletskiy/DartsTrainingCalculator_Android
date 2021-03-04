package com.beletskiy.dartstrainingcalculator.fragments.scores

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.beletskiy.dartstrainingcalculator.databinding.FragmentScoresBinding

class ScoresFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScoresBinding.inflate(inflater)
        val view = binding.root

        // FIXME: temporary for testing purposes
        binding.button.setOnClickListener {
           view.findNavController().navigate(ScoresFragmentDirections.actionScoresFragmentToTossFragment())
        }

        return binding.root
    }

}