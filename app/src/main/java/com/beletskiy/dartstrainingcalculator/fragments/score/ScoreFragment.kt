package com.beletskiy.dartstrainingcalculator.fragments.score

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.databinding.FragmentScoreBinding
import com.beletskiy.dartstrainingcalculator.utils.PreferenceUtils
import com.beletskiy.dartstrainingcalculator.utils.observeInLifecycle
import kotlinx.coroutines.flow.onEach

class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding
    private val scoreViewModel: ScoreViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ScoreViewModel.Factory(requireActivity().application)
        ).get(ScoreViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        binding = FragmentScoreBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.vm = scoreViewModel

        readGameSettings()

        //<editor-fold desc="setting up RecyclerView">
        // binding RecyclerView with ListAdapter
        val scoreAdapter = ScoreAdapter()
        binding.recyclerView.adapter = scoreAdapter

        // GridLayoutManager with default vertical orientation and 3 number of columns
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        // set LayoutManager to RecyclerView
        binding.recyclerView.layoutManager = gridLayoutManager

        // setting ViewModel as data supplier for RecyclerView
        scoreViewModel.tossList.observe(viewLifecycleOwner, {
            it?.let {
                scoreAdapter.submitList(it)
            }
        })
        //</editor-fold>

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // navigate to TossFragment by clicking "ADD A THROW" button
        binding.addTossButton.setOnClickListener {
            findNavController().navigate(ScoreFragmentDirections.actionScoreFragmentToTossFragment())
        }
        // update Toolbar text with the current score
        scoreViewModel.scoresTitle.observe(viewLifecycleOwner, {
            it?.let { text ->
                (activity as AppCompatActivity?)?.supportActionBar?.title = text
            }
        })
        // receiving events from ViewModel - adding new Toss triggers RecyclerView scrolling
        scoreViewModel.eventsFlow.onEach { event ->
            if (event is ScoreViewModel.Event.OnNewTossAdded) {
                binding.recyclerView.smoothScrollToPosition(event.position)
            }
        }.observeInLifecycle(viewLifecycleOwner)
    }

    /** adds menu with "Restart game" to the toolbar */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.score_menu, menu)
    }

    /** called when User clicked "Restart game" or "Undo" in the toolbar */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_game -> {
                restartGameWithConfirmation()
                true
            }
            R.id.undo -> {
                scoreViewModel.undoLastThrow()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** asks if User is sure and wants to restart the game and loose all progress */
    private fun restartGameWithConfirmation() {
        // don't ask for confirmation if there is no throws yet or if game is over
        if (scoreViewModel.tossList.value?.size == 0 || scoreViewModel.isGameOver.value == true) {
            scoreViewModel.restartGame()
            return
        }
        activity?.let {
            AlertDialog.Builder(it).run {
                setTitle(getString(R.string.restart_game))
                setMessage(getString(R.string.all_progress_will_be_lost))
                setPositiveButton(getString(R.string.confirm)) { _, _ ->
                    scoreViewModel.restartGame()
                }
                setNegativeButton(getString(R.string.cancel)) { _, _ ->
                }
                create()
                show()
            }
        }
    }

    /** Gets game points value (301, 501, etc) from SharedPreferences */
    private fun readGameSettings() {
        val newGameTotalPoints = PreferenceUtils.getStartPoints(requireContext())
        scoreViewModel.onNewStartPoints(newGameTotalPoints)
    }

}

