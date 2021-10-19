package com.beletskiy.dartstrainingcalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_table")
data class SavedGame(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val points: Int,
)
