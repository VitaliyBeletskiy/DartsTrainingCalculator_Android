package com.beletskiy.dartstrainingcalculator.fragments.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.beletskiy.dartstrainingcalculator.data.DartsRepository
import com.beletskiy.dartstrainingcalculator.data.GameAndTosses
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject internal constructor(
    private val dartsRepository: DartsRepository
) : ViewModel() {

    val gameAndTossesList = dartsRepository.gameAndTossesList.asLiveData()
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

    /** deletes one SavedGame (User action from row's pop-up menu) */
    fun deleteSavedGame(gameId: Long) {
        viewModelScope.launch {
            dartsRepository.deleteSavedGame(gameId)
        }
    }
}
