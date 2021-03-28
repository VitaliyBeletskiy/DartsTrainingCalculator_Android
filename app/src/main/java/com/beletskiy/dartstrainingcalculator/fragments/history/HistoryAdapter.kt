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
import com.beletskiy.dartstrainingcalculator.database.GameAndTosses
import com.beletskiy.dartstrainingcalculator.databinding.ItemSavedGameBinding
import com.beletskiy.dartstrainingcalculator.utils.TAG

class HistoryAdapter(private val rowClickListener: RowClickListener) :
    ListAdapter<GameAndTosses, HistoryAdapter.GameAndTossesViewHolder>(GameAndTossesDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameAndTossesViewHolder {
        return GameAndTossesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GameAndTossesViewHolder, position: Int) {
        val gameAndTossesItem = getItem(position)
        holder.bind(gameAndTossesItem, rowClickListener)
    }

    /// ViewHolder for GameAndTosses
    class GameAndTossesViewHolder(private var binding: ItemSavedGameBinding) :
        RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {

        private var clickListener: RowClickListener? = null

        companion object {
            fun from(parent: ViewGroup): GameAndTossesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSavedGameBinding.inflate(layoutInflater, parent, false)
                return GameAndTossesViewHolder(binding)
            }
        }

        fun bind(gameAndTosses: GameAndTosses, rowClickListener: RowClickListener) {
            binding.gameAndTosses = gameAndTosses
            clickListener = rowClickListener

            // listen to and handle "User clicked on entire row in RecyclerView" event
            binding.setRowClickListener {
                // TODO: check "not-null assertion operator (!!)"
                rowClickListener.onRowClicked(binding.gameAndTosses!!)
            }
            // set up Pop-up Menu for the particular row
            binding.setMenuClickListener {
                it?.let {
                    showPopupMenu(it)
                }
            }

            binding.executePendingBindings()
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
                // TODO: check "not-null assertion operator (!!)"
                clickListener?.onPopupMenuItemClicked(binding.gameAndTosses!!.savedGame!!.id)
                return true
            }
            return false
        }
    }

    // required for ListAdapter
    class GameAndTossesDiffItemCallback : DiffUtil.ItemCallback<GameAndTosses>() {

        override fun areItemsTheSame(oldItem: GameAndTosses, newItem: GameAndTosses): Boolean =
            oldItem.savedGame?.id == newItem.savedGame?.id

        override fun areContentsTheSame(oldItem: GameAndTosses, newItem: GameAndTosses): Boolean =
            oldItem == newItem

    }

    // interface to handle user action for the particular row (row click, pop-up menu click)
    interface RowClickListener {
        fun onPopupMenuItemClicked(gameId: Long)

        fun onRowClicked(gameAndTosses: GameAndTosses)
    }

}




