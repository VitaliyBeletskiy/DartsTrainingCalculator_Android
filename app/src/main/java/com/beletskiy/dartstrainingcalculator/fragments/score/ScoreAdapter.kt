package com.beletskiy.dartstrainingcalculator.fragments.score

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.databinding.ItemTossBinding

class ScoreAdapter : ListAdapter<Toss, TossViewHolder>(TossDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TossViewHolder {
        return TossViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TossViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // creates and submits a new list to notify ListAdapter that there were some changes
    override fun submitList(list: List<Toss>?) {
        super.submitList(list?.toList())
    }

}

class TossViewHolder(private var binding: ItemTossBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(toss: Toss) {
        binding.toss = toss
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): TossViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemTossBinding.inflate(layoutInflater, parent, false)
            return TossViewHolder(binding)
        }
    }
}

class TossDiffItemCallback : DiffUtil.ItemCallback<Toss>() {

    override fun areItemsTheSame(oldItem: Toss, newItem: Toss): Boolean =
        oldItem.number == newItem.number

    // This method is called only if areItemsTheSame(T, T) returns true for these items.
    override fun areContentsTheSame(oldItem: Toss, newItem: Toss): Boolean = oldItem == newItem

}