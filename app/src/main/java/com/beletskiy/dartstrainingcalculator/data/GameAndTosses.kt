package com.beletskiy.dartstrainingcalculator.data

import androidx.room.Embedded
import androidx.room.Relation

data class GameAndTosses(
    @Embedded
    var savedGame: SavedGame? = null,
    @Relation(parentColumn = "id", entityColumn = "game_id")
    var savedTossList: List<SavedToss> = ArrayList(),
)