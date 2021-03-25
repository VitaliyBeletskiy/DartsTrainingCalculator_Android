package com.beletskiy.dartstrainingcalculator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_table")
data class SavedGame(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val points: Int,
)

@Entity(tableName = "toss_table")
data class SavedToss(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "game_id")
    val gameId: Long,
    val number: Int,
    val section: Int,
    val ring: Int,
)
