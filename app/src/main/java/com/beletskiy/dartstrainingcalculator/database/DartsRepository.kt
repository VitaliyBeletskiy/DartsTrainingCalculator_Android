package com.beletskiy.dartstrainingcalculator.database

import android.content.Context
import androidx.annotation.WorkerThread
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.database.DartsDatabase.Companion.getDatabase
import kotlinx.coroutines.flow.Flow

class DartsRepository(context: Context) {

    private val dartsDatabase = getDatabase(context)

    //val savedGameList: Flow<List<SavedGame>> = dartsDatabase.savedGameDao().getAll()
    val gameAndTossesList: Flow<List<GameAndTosses>> = dartsDatabase.savedGameDao().getAllGamesAndTosses()

    /// saves the finished game to database
    @WorkerThread
    suspend fun saveGame(gamePoints: Int, tossList: List<Toss>) {
        val savedGame = SavedGame(points = gamePoints)
        // save game first, get gameId
        val gameId = dartsDatabase.savedGameDao().insertGame(savedGame)
        // convert List<Toss> to List<SavedToss>
        val savedTossList = convertTossListToSavedTossList(gameId, tossList)
        // save List<SavedToss> to database
        dartsDatabase.savedTossDao().insertTosses(savedTossList)
    }

    // empties database = deletes all data
    @WorkerThread
    suspend fun deleteAllData() {
        dartsDatabase.run {
            savedGameDao().deleteAll()
        }
    }

    // deletes one SavedGamed with all its SavedTosses
    @WorkerThread
    suspend fun deleteSavedGame(gameId: Long) {
        dartsDatabase.run {
            savedGameDao().deleteGame(gameId)
        }
    }

    // deletes the last SavedGamed with all its SavedTosses
    @WorkerThread
    suspend fun deleteLastSavedGame() {
        dartsDatabase.run {
            savedGameDao().deleteLastGame()
        }
    }

    /// converts the list of Toss to the list of SavedToss (to save in database)
    private fun convertTossListToSavedTossList(
        gameId: Long,
        tossList: List<Toss>
    ): List<SavedToss> {
        return tossList.map {
            SavedToss(
                gameId = gameId,
                number = it.number,
                counted = it.counted,
                section = it.section.ordinal,
                ring = it.ring.ordinal
            )
        }
    }

}