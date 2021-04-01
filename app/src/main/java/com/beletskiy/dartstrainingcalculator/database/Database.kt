package com.beletskiy.dartstrainingcalculator.database

import android.content.Context
import androidx.room.*
import com.beletskiy.dartstrainingcalculator.utils.DATABASE_NAME
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

    //    @Query("DELETE FROM game_table ORDER BY timestamp DESC LIMIT 1")
    @Query("DELETE FROM game_table WHERE id = (SELECT MAX(id) FROM game_table)")
    suspend fun deleteLastGame()

    /*    @Query("SELECT * FROM game_table")
    fun getAll(): Flow<List<SavedGame>>*/
}

@Dao
interface SavedTossDao {

    @Insert
    suspend fun insertTosses(savedTossList: List<SavedToss>)

/*    // Don't need this any mose as we read 'GameAndTosses' from database
    @Query("SELECT * FROM toss_table WHERE game_id = :gameId")
    fun getAllForGame(gameId: Long): Flow<List<SavedToss>>*/

/*  // Don't need DELETE queries any more due to cascade deletion from SavedGame using foreign key
    @Query("DELETE FROM toss_table")
    suspend fun deleteAll()

    @Query("DELETE FROM toss_table WHERE game_id = :gameId")
    suspend fun deleteAllForGame(gameId: Long)*/
}

@Database(entities = [SavedGame::class, SavedToss::class], version = 1, exportSchema = false)
abstract class DartsDatabase : RoomDatabase() {

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
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE!!
        }
    }
}