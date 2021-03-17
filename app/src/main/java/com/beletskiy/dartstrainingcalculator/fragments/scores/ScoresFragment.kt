package com.beletskiy.dartstrainingcalculator.fragments.scores

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.databinding.FragmentScoresBinding
import com.beletskiy.dartstrainingcalculator.utils.TAG

class ScoresFragment : Fragment() {

    private lateinit var binding: FragmentScoresBinding
    private val scoresViewModel: ScoresViewModel by lazy {
        ViewModelProvider(this).get(ScoresViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScoresBinding.inflate(inflater)
        binding.scoresViewModel = scoresViewModel
        binding.lifecycleOwner = this

        // binding RecyclerView with ListAdapter and setting ViewModel as data supplier
        val scoresAdapter = ScoresAdapter()
        binding.recyclerView.adapter = scoresAdapter
        scoresViewModel.tossList.observe(viewLifecycleOwner, {
            it?.let {
                scoresAdapter.submitList(it)
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // navigate to TossFragment by clicking [fab]
        binding.fab.setOnClickListener {
            findNavController().navigate(ScoresFragmentDirections.actionScoresFragmentToTossFragment())
        }

        // getting new [Toss] from TossFragment
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Toss>(NEW_TOSS)
            ?.observe(viewLifecycleOwner, {
                scoresViewModel.onNewTossCreated(it)
            })
    }

    /// const for previousBackStackEntry/currentBackStackEntry (getting a new Toss from TossFragment)
    companion object {
        const val NEW_TOSS = "NEW_TOSS"
    }

}