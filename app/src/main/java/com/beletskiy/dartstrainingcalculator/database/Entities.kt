package com.beletskiy.dartstrainingcalculator.database

import androidx.room.*
import com.beletskiy.dartstrainingcalculator.data.Toss

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
    val counted: Boolean,
    val section: Int,
    val ring: Int,
) {

    fun toToss(): Toss {
        return Toss(
            number = number,
            counted = counted,
            section = Toss.Section.values()[section],
            ring = Toss.Ring.values()[ring]
        )
    }

}

data class GameAndTosses(
    @Embedded
    var savedGame: SavedGame? = null,
    @Relation(parentColumn = "id", entityColumn = "game_id")
    var savedTossList: List<SavedToss> = ArrayList(),
)