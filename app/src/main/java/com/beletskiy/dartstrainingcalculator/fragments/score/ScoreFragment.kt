package com.beletskiy.dartstrainingcalculator.fragments.score

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.databinding.FragmentScoreBinding
import com.beletskiy.dartstrainingcalculator.utils.TAG

class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding
    private val scoreViewModel: ScoreViewModel by lazy {
        ViewModelProvider(
            this,
            ScoreViewModel.Factory(21)
//            ScoreViewModel.Factory(selectedGame)
        ).get(ScoreViewModel::class.java)
    }
    private var selectedGame: Int = 501

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentScoreBinding.inflate(inflater)
        readSettings()
        binding.scoreViewModel = scoreViewModel
        binding.lifecycleOwner = this

        // binding RecyclerView with ListAdapter and
        val scoreAdapter = ScoreAdapter()
        binding.recyclerView.adapter = scoreAdapter

        // GridLayoutManager with default vertical orientation and 3 number of columns
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        // set LayoutManager to RecyclerView
        binding.recyclerView.layoutManager = gridLayoutManager

        // setting ViewModel as data supplier
        scoreViewModel.tossList.observe(viewLifecycleOwner, {
            it?.let {
                scoreAdapter.submitList(it)
            }
        })

        // navigate to TossFragment by clicking [fab]
        binding.fab.setOnClickListener {
            findNavController().navigate(ScoreFragmentDirections.actionScoreFragmentToTossFragment())
        }

        // getting new [Toss] from TossFragment
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Toss>(NEW_TOSS)
            ?.observe(viewLifecycleOwner, {
                scoreViewModel.onNewTossCreated(it)

                // FIXME: KLUDGE!!! otherwise the previous new Toss value added every time we come
                // back to ScoreFragment
                findNavController()
                    .currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<Toss>(ScoreFragment.NEW_TOSS)
            })

        return binding.root
    }

    private fun readSettings() {
        val context = requireContext()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val gameString = sharedPreferences.getString(getString(R.string.game_key), "501")
        selectedGame = gameString?.toInt() ?: selectedGame

        val preventSleep =
            sharedPreferences.getBoolean(getString(R.string.prevent_sleep_key), false)

        //Log.i(TAG, "ScoreFragment.readSettings(): selectedGame = $selectedGame, preventSleep = $preventSleep")
    }


    /// const for savedStateHandle.getLiveData (getting a new Toss from TossFragment)
    companion object {
        const val NEW_TOSS = "NEW_TOSS"
    }

}