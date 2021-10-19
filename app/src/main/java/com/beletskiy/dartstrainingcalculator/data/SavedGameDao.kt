package com.beletskiy.dartstrainingcalculator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedGameDao {

    @Transaction
    @Query("SELECT * FROM game_table")
    fun getAllGamesAndTosses(): Flow<List<GameAndTosses>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(savedGame: SavedGame): Long

    @Query("DELETE FROM game_table WHERE id = :id")
    suspend fun deleteGame(id: Long)

    @Query("DELETE FROM game_table")
    suspend fun deleteAll()

    @Query("DELETE FROM game_table WHERE id = (SELECT MAX(id) FROM game_table)")
    suspend fun deleteLastGame()
}
