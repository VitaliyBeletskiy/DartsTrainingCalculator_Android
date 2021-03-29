package com.beletskiy.dartstrainingcalculator.fragments.score

import android.app.Application
import androidx.lifecycle.*
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.database.DartsRepository
import com.beletskiy.dartstrainingcalculator.utils.inSeriesOf3
import kotlinx.coroutines.launch
import kotlin.random.Random

class ScoreViewModel(private var gameTotalScore: Int, application: Application) :
    AndroidViewModel(application) {

    private val dartsRepository = DartsRepository(application)

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
    private var throwPositionInSeries: Int = 0

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
        throwPositionInSeries = 0                     // reset points counter
    }

    // when User added a new throw
    fun onNewTossCreated(newToss: Toss) {

        // TODO: 23/03/2021 exception or notify User??? Or do I need it at all?
        if (_scoreAfterThrow.value == null) return

        newToss.number = (_tossList.value?.size ?: 0) + 1
        newToss.counted = true
        _tossList.value?.add(newToss)
        _tossList.value = _tossList.value

        throwPositionInSeries = newToss.number.inSeriesOf3  // 1, 2 or 3
        _scoreAfterThrow.value = _scoreAfterThrow.value?.minus(newToss.value)


        // check if game is over
        if (_scoreAfterThrow.value == 0 &&
            (newToss.ring == Toss.Ring.X2 || newToss.section == Toss.Section.INNER_BULLSEYE)
        ) {
            // TODO: Game is over - inform user

            //  save the game to database
            viewModelScope.launch {
                dartsRepository.saveGame(gameTotalScore, _tossList.value ?: emptyList())
            }
            _isGameOver.value = true
            return
        }
        // whether the entire series counts or not
        if (1 >= _scoreAfterThrow.value!!) {
            // make last x throws not "counted"
            _tossList.value?.takeLast(throwPositionInSeries)?.forEach {
                it.counted = false
            }
            // add "missed" throws to complete current series
            completeSeriesWithMissedThrows(throwPositionInSeries)
            _scoreAfterThrow.value = scoreAfterSeries
            return
        }
        // if it was the third throw in series - update scoreAfterSeries
        if (3 == throwPositionInSeries) {
            scoreAfterSeries = _scoreAfterThrow.value!!
        }

    }

    // if current series needs to be skipped, adds "missed" throws to make 3 throws in current series
    private fun completeSeriesWithMissedThrows(throwNumberInSeries: Int) {
        for (i in 1..(3 - throwNumberInSeries)) {
            val newTossId = (_tossList.value?.size ?: 0) + 1
            val newToss = Toss(newTossId, false, Toss.Section.MISSED, Toss.Ring.X1)
            _tossList.value?.add(newToss)
        }
        _tossList.value = _tossList.value
    }

    // Factory for constructing ScoreViewModel with parameters
    class Factory(private val game: Int, private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ScoreViewModel(game, app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }

    // TODO  for testing only!!! remove it !!!
    fun saveTestData() {
        val testTossList = ArrayList<Toss>()
        for (i in 1..Random.nextInt(5, 15)) {
            testTossList.add(
                Toss(
                    i,
                    true,
                    Toss.Section.values()[Random.nextInt(1, 21)],
                    Toss.Ring.values()[Random.nextInt(0, 3)],
                )
            )
        }
        viewModelScope.launch {
            dartsRepository.saveGame(Random.nextInt(300, 400), testTossList)
        }
    }
}