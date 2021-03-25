package com.beletskiy.dartstrainingcalculator.database

import android.content.Context
import androidx.room.*
import com.beletskiy.dartstrainingcalculator.utils.DATABASE_NAME

@Dao
interface SavedGameDao {

    @Query("SELECT * FROM game_table")
    fun getAll(): List<SavedGame>

    @Insert
    suspend fun insertGame(savedGame: SavedGame)

    @Query("DELETE FROM game_table")
    suspend fun deleteAll()
}

@Dao
interface SavedTossDao {

    @Query("SELECT * FROM toss_table WHERE game_id = :gameId")
    fun getAllForGame(gameId: Long): List<SavedToss>

    @Insert
    suspend fun insertTosses(savedTossList: List<SavedToss>)

    @Query("DELETE FROM toss_table")
    suspend fun deleteAll()

    @Query("DELETE FROM toss_table WHERE game_id = :gameId")
    suspend fun deleteAllForGame(gameId: Long)
}

@Database(entities = [SavedGame::class, SavedToss::class], version = 1, exportSchema = false)
abstract class DartsDatabase: RoomDatabase() {

    abstract fun savedGameDao(): SavedGameDao
    abstract fun savedTossDao(): SavedTossDao

    companion object {
        @Volatile
        private var INSTANCE: DartsDatabase? = null

        fun getDatabase(context: Context): DartsDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,  // Application Context, not Activity Context
                        DartsDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}