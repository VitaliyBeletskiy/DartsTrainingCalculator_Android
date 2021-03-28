package com.beletskiy.dartstrainingcalculator.database

import androidx.room.*

@Entity(tableName = "game_table")
data class SavedGame(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val points: Int,
)

@Entity(
    tableName = "toss_table",
    foreignKeys = [ForeignKey(
        entity = SavedGame::class,
        parentColumns = ["id"],
        childColumns = ["game_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class SavedToss(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "game_id", index = true) val gameId: Long,
    val number: Int,
    val section: Int,
    val ring: Int,
)

data class GameAndTosses(
    @Embedded
    var savedGame: SavedGame? = null,
    @Relation(parentColumn = "id", entityColumn = "game_id")
    var savedTossList: List<SavedToss> = ArrayList(),
)