package com.beletskiy.dartstrainingcalculator.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DartsRepository @Inject constructor(
    private val savedGameDao: SavedGameDao,
    private val savedTossDao: SavedTossDao
) {

    val gameAndTossesList = savedGameDao.getAllGamesAndTosses()

    /** Saves the finished game to database. */
    @WorkerThread
    suspend fun saveGame(gamePoints: Int, tossList: List<Toss>) {
        val savedGame = SavedGame(points = gamePoints)
        // save game first, get gameId
        val gameId = savedGameDao.insertGame(savedGame)
        // convert List<Toss> to List<SavedToss>
        val savedTossList = tossList.convertToSavedTossList(gameId)
        // save List<SavedToss> to database
        withContext(Dispatchers.IO) {
            savedTossDao.insertTosses(savedTossList)
        }
    }

    /** Empties database = deletes all data. */
    @WorkerThread
    suspend fun deleteAllData() {
        withContext(Dispatchers.IO) {
            savedGameDao.deleteAll()
        }
    }

    /** Deletes one SavedGamed with all its SavedTosses. */
    @WorkerThread
    suspend fun deleteSavedGame(gameId: Long) {
        withContext(Dispatchers.IO) {
            savedGameDao.deleteGame(gameId)
        }
    }

    /** Deletes the last SavedGamed with all its SavedTosses. */
    @WorkerThread
    suspend fun deleteLastSavedGame() {
        withContext(Dispatchers.IO) {
            savedGameDao.deleteLastGame()
        }
    }

    /** Converts the list of Toss to the list of SavedToss (to save in database). */
    private fun List<Toss>.convertToSavedTossList(gameId: Long): List<SavedToss> {
        return map {
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
