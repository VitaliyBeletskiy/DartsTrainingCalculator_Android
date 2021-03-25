package com.beletskiy.dartstrainingcalculator.database

import android.content.Context
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.database.DartsDatabase.Companion.getDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DartsRepository(context: Context) {

    private val dartsDatabase = getDatabase(context)

    /// saves the finished game to database
    fun saveGame(gamePoints: Int, tossList: List<Toss>) {
        val savedGame = SavedGame(points = gamePoints)
        CoroutineScope(Dispatchers.IO).launch {
            // save game first, get gameId
            val gameId = dartsDatabase.savedGameDao().insertGame(savedGame)
            // convert List<Toss> to List<SavedToss>
            val savedTossList = convertTossListToSavedTossList(gameId, tossList)
            // save List<SavedToss> to database
            dartsDatabase.savedTossDao().insertTosses(savedTossList)
        }
    }

    /// converts the list of Toss to the list of SavedToss (to save in database)
    private fun convertTossListToSavedTossList(gameId: Long, tossList: List<Toss>): List<SavedToss> {
        return tossList.map {
            SavedToss(
                gameId = gameId,
                number = it.number,
                section = it.section.ordinal,
                ring = it.ring.ordinal
            )
        }
    }

}