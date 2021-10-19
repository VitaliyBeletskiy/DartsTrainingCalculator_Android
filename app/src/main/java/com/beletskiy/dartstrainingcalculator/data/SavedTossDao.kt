package com.beletskiy.dartstrainingcalculator.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface SavedTossDao {

    @Insert
    suspend fun insertTosses(savedTossList: List<SavedToss>)
}
