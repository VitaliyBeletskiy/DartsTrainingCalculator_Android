package com.beletskiy.dartstrainingcalculator.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "toss_table",
    foreignKeys = [ForeignKey(
        entity = SavedGame::class,
        parentColumns = ["id"],
        childColumns = ["game_id"],
        onDelete = ForeignKey.CASCADE
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