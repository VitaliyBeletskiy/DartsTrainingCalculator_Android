package com.beletskiy.dartstrainingcalculator.fragments.gamedetails

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.databinding.FragmentGameDetailsBinding
import com.beletskiy.dartstrainingcalculator.utils.TAG

class GameDetailsFragment : Fragment() {

    private lateinit var binding: FragmentGameDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        binding = FragmentGameDetailsBinding.inflate(inflater)

        val args: GameDetailsFragmentArgs by navArgs()

        val gameId = args.gameId
        binding.textView.text = "Game with id = $gameId"

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.game_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.share) {
            // TODO: add code for "Share"
            Log.i(TAG, "GameDetailsFragment: Share clicked ")
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

}