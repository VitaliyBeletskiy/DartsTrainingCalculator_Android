package com.beletskiy.dartstrainingcalculator.fragments.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beletskiy.dartstrainingcalculator.database.SavedGame
import com.beletskiy.dartstrainingcalculator.databinding.ItemSavedGameBinding

class HistoryAdapter : ListAdapter<SavedGame, SavedGameViewHolder>(SavedGameDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedGameViewHolder {
        return SavedGameViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SavedGameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // creates and submits a new list to notify ListAdapter that there were some changes
    override fun submitList(list: List<SavedGame>?) {
        super.submitList(list?.toList())
    }
}

class SavedGameViewHolder(private var binding: ItemSavedGameBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(savedGame: SavedGame) {
        binding.savedGame = savedGame
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): SavedGameViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSavedGameBinding.inflate(layoutInflater, parent, false)
            return SavedGameViewHolder(binding)
        }
    }
}

class SavedGameDiffItemCallback : DiffUtil.ItemCallback<SavedGame>() {

    override fun areItemsTheSame(oldItem: SavedGame, newItem: SavedGame): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SavedGame, newItem: SavedGame): Boolean =
        oldItem == newItem

}