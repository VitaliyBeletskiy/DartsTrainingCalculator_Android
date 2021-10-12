package com.beletskiy.dartstrainingcalculator.data

import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class DartsRepository(context: Context) {

    private val dartsDatabase = DartsDatabase.getInstance(context)

    val gameAndTossesList: Flow<List<GameAndTosses>> =
        dartsDatabase.savedGameDao().getAllGamesAndTosses()

    /** Saves the finished game to database. */
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

    /** Empties database = deletes all data. */
    @WorkerThread
    suspend fun deleteAllData() {
        dartsDatabase.run {
            savedGameDao().deleteAll()
        }
    }

    /** Deletes one SavedGamed with all its SavedTosses. */
    @WorkerThread
    suspend fun deleteSavedGame(gameId: Long) {
        dartsDatabase.run {
            savedGameDao().deleteGame(gameId)
        }
    }

    /** Deletes the last SavedGamed with all its SavedTosses. */
    @WorkerThread
    suspend fun deleteLastSavedGame() {
        dartsDatabase.run {
            savedGameDao().deleteLastGame()
        }
    }

    /** Converts the list of Toss to the list of SavedToss (to save in database). */
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