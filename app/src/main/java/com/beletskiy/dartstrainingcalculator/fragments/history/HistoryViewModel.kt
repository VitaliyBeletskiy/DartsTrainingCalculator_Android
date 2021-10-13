package com.beletskiy.dartstrainingcalculator.fragments.history

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.beletskiy.dartstrainingcalculator.data.DartsRepository
import com.beletskiy.dartstrainingcalculator.data.GameAndTosses
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel@Inject internal constructor(
    private val dartsRepository: DartsRepository
) : ViewModel() {

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

}