package com.beletskiy.dartstrainingcalculator.fragments.scores

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.beletskiy.dartstrainingcalculator.databinding.FragmentScoresBinding
import com.beletskiy.dartstrainingcalculator.utils.TAG

class ScoresFragment : Fragment() {

    private lateinit var binding : FragmentScoresBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScoresBinding.inflate(inflater)
        val scoresViewModel = ViewModelProvider(this).get(ScoresViewModel::class.java)
        binding.scoresViewModel = scoresViewModel
        binding.lifecycleOwner = this

        // TossFragment MIGHT send 'newToss' argument (so, it's nullable)
        val newToss = ScoresFragmentArgs.fromBundle(requireArguments()).newToss
        Log.i(TAG, "ScoresFragment just got a new Toss = $newToss")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FIXME: temporary for testing purposes
        binding.button.setOnClickListener {
            view.findNavController().navigate(ScoresFragmentDirections.actionScoresFragmentToTossFragment())
        }
    }

}