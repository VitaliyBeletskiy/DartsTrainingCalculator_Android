package com.beletskiy.dartstrainingcalculator.fragments.history

import android.app.Application
import androidx.lifecycle.*
import com.beletskiy.dartstrainingcalculator.database.DartsRepository
import com.beletskiy.dartstrainingcalculator.database.GameAndTosses
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val dartsRepository = DartsRepository(application)
    val gameAndTossesList: LiveData<List<GameAndTosses>> = dartsRepository.gameAndTossesList.asLiveData()
    var selectedGameAndTosses = MutableLiveData<GameAndTosses>()

    /**
     * empties database and refreshes RecyclerView
     * called when User clicked "Delete All" in History Fragment
     */
    fun deleteAllData() {
        viewModelScope.launch {
            dartsRepository.deleteAllData()
        }
    }

    /// deletes one SavedGame (User action from row's pop-up menu)
    fun deleteSavedGame(gameId: Long) {
        viewModelScope.launch {
            dartsRepository.deleteSavedGame(gameId)
        }
    }

    // Factory for constructing HistoryViewModel with parameters
    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HistoryViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}