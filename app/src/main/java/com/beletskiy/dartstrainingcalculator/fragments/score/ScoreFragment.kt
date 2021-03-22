package com.beletskiy.dartstrainingcalculator.fragments.score

import android.app.AlertDialog
import android.content.DialogInterface
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

        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.score_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_game -> {
                restartGameWithConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun restartGameWithConfirmation() {
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

    /// const for savedStateHandle.getLiveData (getting a new Toss from TossFragment)
    companion object {
        const val NEW_TOSS = "NEW_TOSS"
    }

}