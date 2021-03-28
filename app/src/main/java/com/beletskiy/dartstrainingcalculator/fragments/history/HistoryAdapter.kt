package com.beletskiy.dartstrainingcalculator.fragments.history

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.database.SavedGame
import com.beletskiy.dartstrainingcalculator.databinding.ItemSavedGameBinding
import com.beletskiy.dartstrainingcalculator.utils.TAG

class HistoryAdapter :
    ListAdapter<SavedGame, SavedGameViewHolder>(SavedGameDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedGameViewHolder {
        return SavedGameViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SavedGameViewHolder, position: Int) {
        val savedGameItem = getItem(position)
        holder.bind(savedGameItem)
    }

}

class SavedGameViewHolder(private var binding: ItemSavedGameBinding) :
    RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {

    companion object {
        fun from(parent: ViewGroup): SavedGameViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSavedGameBinding.inflate(layoutInflater, parent, false)
            return SavedGameViewHolder(binding)
        }
    }

    fun bind(savedGame: SavedGame) {
        binding.savedGame = savedGame
        // listen to and handle "User clicked on entire row in RecyclerView" event
        binding.setRowClickListener { view ->
            binding.savedGame?.id?.let { gameId ->
                // TODO: navigate to GameDetailsFragment
                Log.i(TAG, "Navigate to GameDetails for id = $gameId")
                navigateToPlant(gameId, view)
            }
        }
        // set up Pop-up Menu for particular row
        binding.setMenuClickListener {
            it?.let {
                showPopupMenu(it)
            }
        }
        binding.executePendingBindings()
    }

    // once User clicked on the row - navigate to GameDetailsFragment
    private fun navigateToPlant(gameId: Long, view: View) {
        val direction = HistoryFragmentDirections.actionHistoryToGameDetails(gameId)
        view.findNavController().navigate(direction)
    }

    // inflates and shows Pop-up menu when User clicks on menu button in particular row
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.game_item_menu)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.show()
    }

    // part of PopupMenu.OnMenuItemClickListener interface, handles Pop-up menu actions
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.game_item_delete) {
            Log.i(
                TAG,
                "Somehow going to delete game with id = ${binding.savedGame?.id} at position $adapterPosition"
            )
            return true
        }
        return false
    }
}

// required by ListAdapter
class SavedGameDiffItemCallback : DiffUtil.ItemCallback<SavedGame>() {

    override fun areItemsTheSame(oldItem: SavedGame, newItem: SavedGame): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SavedGame, newItem: SavedGame): Boolean =
        oldItem == newItem

}
