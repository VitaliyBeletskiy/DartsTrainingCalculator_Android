package com.beletskiy.dartstrainingcalculator.data

import androidx.annotation.WorkerThread
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DartsRepository @Inject constructor(
    private val savedGameDao: SavedGameDao,
    private val savedTossDao: SavedTossDao
) {

    val gameAndTossesList = savedGameDao.getAllGamesAndTosses()

    // FIXME: а надо тут  @WorkerThread ???
    /** Saves the finished game to database. */
    @WorkerThread
    suspend fun saveGame(gamePoints: Int, tossList: List<Toss>) {
        val savedGame = SavedGame(points = gamePoints)
        // save game first, get gameId
        val gameId = savedGameDao.insertGame(savedGame)
        // convert List<Toss> to List<SavedToss>
        val savedTossList = convertTossListToSavedTossList(gameId, tossList)
        // save List<SavedToss> to database
        savedTossDao.insertTosses(savedTossList)
    }

    /** Empties database = deletes all data. */
    @WorkerThread
    suspend fun deleteAllData() {
        savedGameDao.deleteAll()
    }

    /** Deletes one SavedGamed with all its SavedTosses. */
    @WorkerThread
    suspend fun deleteSavedGame(gameId: Long) {
        savedGameDao.deleteGame(gameId)
    }

    /** Deletes the last SavedGamed with all its SavedTosses. */
    @WorkerThread
    suspend fun deleteLastSavedGame() {
        savedGameDao.deleteLastGame()
    }

    // FIXME: перевести это в inline fun
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