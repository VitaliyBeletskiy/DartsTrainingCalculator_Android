package com.beletskiy.dartstrainingcalculator.fragments.gamedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beletskiy.dartstrainingcalculator.data.SavedToss
import com.beletskiy.dartstrainingcalculator.databinding.ItemDetailedTossBinding

class GameDetailedAdapter : ListAdapter<SavedToss, GameDetailedAdapter.SavedTossViewHolder>(SavedTossDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedTossViewHolder {
        return SavedTossViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SavedTossViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SavedTossViewHolder(private var binding: ItemDetailedTossBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): SavedTossViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDetailedTossBinding.inflate(layoutInflater, parent, false)
                return SavedTossViewHolder(binding)
            }
        }

        fun bind(savedToss: SavedToss) {
            binding.savedToss = savedToss
            binding.executePendingBindings()
        }
    }

    class SavedTossDiffItemCallback : DiffUtil.ItemCallback<SavedToss>() {

        override fun areItemsTheSame(oldItem: SavedToss, newItem: SavedToss): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SavedToss, newItem: SavedToss): Boolean =
            oldItem == newItem
    }
}
