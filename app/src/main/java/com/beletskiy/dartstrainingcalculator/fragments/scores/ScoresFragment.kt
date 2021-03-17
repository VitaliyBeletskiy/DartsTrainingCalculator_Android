package com.beletskiy.dartstrainingcalculator.fragments.scores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.databinding.FragmentScoresBinding


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

        // binding RecyclerView with ListAdapter and
        val scoresAdapter = ScoresAdapter()
        binding.recyclerView.adapter = scoresAdapter

        // GridLayoutManager with default vertical orientation and 3 number of columns
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        // set LayoutManager to RecyclerView
        binding.recyclerView.layoutManager = gridLayoutManager

        // setting ViewModel as data supplier
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

    /// const for savedStateHandle.getLiveData (getting a new Toss from TossFragment)
    companion object {
        const val NEW_TOSS = "NEW_TOSS"
    }

}