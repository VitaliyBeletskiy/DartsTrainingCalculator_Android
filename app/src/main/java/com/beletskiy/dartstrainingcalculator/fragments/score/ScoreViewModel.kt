package com.beletskiy.dartstrainingcalculator.fragments.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.beletskiy.dartstrainingcalculator.data.Toss

class ScoreViewModel(private var gameTotalScore: Int) : ViewModel() {

    // contains all throws
    private val _tossList = MutableLiveData<ArrayList<Toss>>(ArrayList())
    val tossList: LiveData<ArrayList<Toss>>
        get() = _tossList

    // trigger changes in UI when game is over
    private val _isGameOver = MutableLiveData<Boolean>()
    val isGameOver: LiveData<Boolean>
        get() = _isGameOver

    // current score after the last trow
    private val _scoreAfterThrow = MutableLiveData<Int>()
    val scoreAfterThrow: LiveData<Int>
        get() = _scoreAfterThrow

    // score after the most recent series of 3 trows
    private var scoreAfterSeries: Int = 0

    // throw number in series of 3
    private var throwNumberInSeries: Int = 0

    init {
        restartGame()
    }

    // when User chosen another Game (f.e. 301 instead of 501)
    fun onGameChanged(newTotalPoints: Int) {
        gameTotalScore = newTotalPoints
        restartGame()
    }

    // restarts the game
    fun restartGame() {
        _tossList.value = ArrayList()               // empty array of "Toss"
        _isGameOver.value = false                   // game is NOT over
        scoreAfterSeries = gameTotalScore           // reset points counter
        _scoreAfterThrow.value = gameTotalScore     // reset points counter
        throwNumberInSeries = 0                     // reset points counter
    }

    // when User added a new throw
    fun onNewTossCreated(newToss: Toss) {

        // TODO: 23/03/2021 exception or notify User??? Or do I need it at all?
        if (_scoreAfterThrow.value == null) return

        newToss.number = (_tossList.value?.size ?: 0) + 1
        _tossList.value?.add(newToss)
        _tossList.value = _tossList.value

        throwNumberInSeries = newToss.number % 3
        _scoreAfterThrow.value = _scoreAfterThrow.value?.minus(newToss.value)

        // check if game is over
        if (_scoreAfterThrow.value == 0 && (newToss.ring == Toss.Ring.X2 || newToss.section == Toss.Section.INNER_BULLSEYE)) {
            // TODO: Game is over - inform user and add game to history
            _isGameOver.value = true
            return
        }
        // whether the entire series counts or not
        if (1 >= _scoreAfterThrow.value!!) {
            // add "missed" throws to complete current series
            completeSeriesWIthMissedThrows(throwNumberInSeries)
            _scoreAfterThrow.value = scoreAfterSeries
            return
        }
        // if it was the third throw in series - update scoreAfterSeries
        if (0 == throwNumberInSeries) {
            scoreAfterSeries = _scoreAfterThrow.value!!
        }

    }

    private fun completeSeriesWIthMissedThrows(throwNumberInSeries: Int) {
        if (0 == throwNumberInSeries) return

        for (i in 1..(3 - throwNumberInSeries)) {
            val newTossId = (_tossList.value?.size ?: 0) + 1
            val newToss = Toss(newTossId, Toss.Section.MISSED, Toss.Ring.X1)
            _tossList.value?.add(newToss)
        }
        _tossList.value = _tossList.value
    }

    // Factory for constructing ScoreViewModel with parameter
    class Factory(private val game: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ScoreViewModel(game) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}