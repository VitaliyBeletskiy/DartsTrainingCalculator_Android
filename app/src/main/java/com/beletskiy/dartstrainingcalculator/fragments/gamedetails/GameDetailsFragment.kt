package com.beletskiy.dartstrainingcalculator.fragments.gamedetails

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.utils.TAG

class GameDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_game_details, container, false)
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