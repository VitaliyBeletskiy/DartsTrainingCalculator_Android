package com.beletskiy.dartstrainingcalculator.fragments.score

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.databinding.FragmentScoreBinding
import com.beletskiy.dartstrainingcalculator.utils.DEFAULT_GAME_VALUE
import com.beletskiy.dartstrainingcalculator.utils.TAG

class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding
    private val scoreViewModel: ScoreViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            ScoreViewModel.Factory(currentGameTotalPoints, activity.application)
        ).get(ScoreViewModel::class.java)
    }

    // what game we are playing - 301 or 501 or custom
    private var currentGameTotalPoints: Int = DEFAULT_GAME_VALUE

    // indicates if we need to restart the current game due to Settings changes
    private var resetGameAsSettingsChanged: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        binding = FragmentScoreBinding.inflate(inflater)
        binding.lifecycleOwner = this
        readGameSettings()
        binding.scoreViewModel = scoreViewModel
        // if Settings changed - notify ViewModel
        if (resetGameAsSettingsChanged) {
            scoreViewModel.onGameChanged(currentGameTotalPoints)
        }

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
                Log.i(TAG, "ScoreFragment.observe was called")
                scoreAdapter.submitList(it)
            }
        })
        //</editor-fold>

        // getting new [Toss] from TossFragment
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Toss>(NEW_TOSS)
            ?.observe(viewLifecycleOwner, {
                it?.let {
                    scoreViewModel.onNewTossCreated(it)

                    // we need a result only once, so we call remove() according to this
                    // https://developer.android.com/guide/navigation/navigation-programmatic#returning_a_result
                    findNavController()
                        .currentBackStackEntry
                        ?.savedStateHandle
                        ?.remove<Toss>(NEW_TOSS)
                }
            })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // navigate to TossFragment by clicking [fab]
        binding.fab.setOnClickListener {
            findNavController().navigate(ScoreFragmentDirections.actionScoreFragmentToTossFragment())
        }
        // update Toolbar text with current score
        scoreViewModel.scoreAfterThrow.observe(viewLifecycleOwner, {
            it?.let {
                getString(
                    R.string.score_fragment_title_bar,
                    it,
                    this.currentGameTotalPoints
                ).also { text ->
                    (activity as AppCompatActivity?)?.supportActionBar?.title = text
                }
            }
        })
    }

    /// adds menu with "Restart game" to the toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.score_menu, menu)
    }

    /// called when User clicked "Restart game" or "Undo" in the toolbar
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
            // TODO  for testing only!!! to remove!!!
            R.id.save_test_data -> {
                scoreViewModel.saveTestData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /// asks if User is sure and wants to restart the game and loose all progress
    private fun restartGameWithConfirmation() {
        // don't ask for confirmation if there is no throws yet or if game is over
        if (scoreViewModel.tossList.value?.size == 0 || scoreViewModel.isGameOver.value == true) {
            scoreViewModel.restartGame()
            return
        }
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(getString(R.string.confirm)) { _, _ ->
                    scoreViewModel.restartGame()
                }
                setNegativeButton(getString(R.string.cancel)) { _, _ ->
                }
            }
            // Set other dialog properties
            builder.setMessage(getString(R.string.all_progress_will_be_lost))
                ?.setTitle(getString(R.string.restart_game))

            // Create the AlertDialog
            builder.create()
        }
        alertDialog?.show()
    }

    /// reads from Preferences what game we play (301 or 501)
    private fun readGameSettings() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val gameString = sharedPreferences.getString(
            getString(R.string.game_key),
            getString(R.string.default_game_value)
        )
        val newGameTotalPoints = gameString?.toInt() ?: currentGameTotalPoints
        resetGameAsSettingsChanged = currentGameTotalPoints != newGameTotalPoints
        currentGameTotalPoints = newGameTotalPoints
    }

    /// const for savedStateHandle.getLiveData (getting a new Toss from TossFragment)
    companion object {
        const val NEW_TOSS = "NEW_TOSS"
    }

}

