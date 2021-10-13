package com.beletskiy.dartstrainingcalculator.fragments.score

import android.content.Context
import androidx.lifecycle.*
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.data.DartsRepository
import com.beletskiy.dartstrainingcalculator.fragments.toss.TossFragment
import com.beletskiy.dartstrainingcalculator.utils.inSeriesOf3
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject internal constructor(
    @ApplicationContext application: Context,
    private val dartsRepository: DartsRepository
) : ViewModel() {

    //region for SingleLiveEvent
    sealed class Event {
        data class OnNewTossAdded(val position: Int) : Event()
        data class ShowSnackBar(val stringId: Int) : Event()
        object NavigateBackToScoreScreen : Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()
    //endregion

    //region Variables for [ScoreFragment]

    private var currentStartPoints = 0

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
    // String for the toolbar title in [ScoresFragment]
    val scoresTitle = Transformations.map(_scoreAfterThrow) {
        it?.let {
            application.getString(R.string.score_fragment_title_bar, it, currentStartPoints)
        }
    }

    // score after the most recent series of 3 trows
    private var scoreAfterSeries: Int = 0

    //endregion

    //region Variables for [TossFragment]

    // contains values for number sections 1-20 as well as 0, Outer Bullseye, Inner Bullseye
    private val _numberSectorChosen = MutableLiveData(Array(23) { false })
    val numberSectorChosen: LiveData<Array<Boolean>>
        get() = _numberSectorChosen

    // contains values for x2 and x3 rings
    private val _ringChosen = MutableLiveData(Array(2) { false })
    val ringChosen: LiveData<Array<Boolean>>
        get() = _ringChosen

    //endregion

    init {
        restartGame()
    }

    //region Functions for [ScoreFragment]

    /** Compares newStartPoints with currentStartPoints and restarts the game if needed. */
    fun onNewStartPoints(newStartPoints: Int) {
        if (newStartPoints != currentStartPoints) {
            currentStartPoints = newStartPoints
            restartGame()
        }
    }

    // restarts the game
    fun restartGame() {
        _tossList.value = ArrayList()               // empty array of "Toss"
        _isGameOver.value = false                   // game is NOT over
        scoreAfterSeries = currentStartPoints       // reset points counter
        _scoreAfterThrow.value = currentStartPoints // reset points counter
    }

    // when User added a new throw
    private fun addNewToss(newToss: Toss) {

        if (_scoreAfterThrow.value == null) {
            throw IllegalArgumentException()
        }

        newToss.number = (_tossList.value?.size ?: 0) + 1
        newToss.counted = true
        _tossList.value?.add(newToss)
        _tossList.value = _tossList.value

        // throw number in series of 3
        val throwPositionInSeries = newToss.number.inSeriesOf3  // 1, 2 or 3
        _scoreAfterThrow.value = _scoreAfterThrow.value?.minus(newToss.value)

        // check if game is over
        if (_scoreAfterThrow.value == 0 &&
            (newToss.ring == Toss.Ring.X2 || newToss.section == Toss.Section.INNER_BULLSEYE)
        ) {
            //  save the game to database
            viewModelScope.launch {
                dartsRepository.saveGame(currentStartPoints, _tossList.value ?: emptyList())
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

        // trigger scrolling inside RecyclerView
        _tossList.value?.size?.let {
            viewModelScope.launch(Dispatchers.Main) {
                eventChannel.send(Event.OnNewTossAdded(it))
            }
        }
    }

    // when User clicks Undo button in ScoreFragment
    fun undoLastThrow() {
        // if there is only one Toss with non-zero value - restart game
        val nonzeroTosses =
            _tossList.value?.fold(0) { sum, item -> sum + (if (item.value > 0) 1 else 0) } ?: 0
        if (nonzeroTosses < 2) {
            restartGame()
            return
        }

        // create an "alias" for cleaner code
        val list = _tossList.value!!

        // keep removing from the end until have removed a Toss with non-zero value
        loop@ while (true) {
            val lastToss = list.last()
            val hasValue = lastToss.value > 0
            list.remove(lastToss)
            if (hasValue) {
                break@loop
            }
        }

        // recalculate score for completed series
        val completedSeries = list.size / 3
        scoreAfterSeries = currentStartPoints - list.subList(0, completedSeries * 3)
            .fold(0) { sum, item -> sum + (if (item.counted) item.value else 0) }

        // Tosses (1 or 2) the incomplete series (the last one) should be switched to counted
        // (as we removed nonzero Toss which caused "bust")
        val lastTossPositionInSeries = list.size % 3  // 0 (nothing to switch), 1 , 2
        for (i in 1..lastTossPositionInSeries) {
            // have to do this to force List Adapter to update UI
            val toss = list[list.size - i].copy(counted = true)
            list[list.size - i] = toss
        }

        _scoreAfterThrow.value = currentStartPoints -
                list.fold(0) { sum, item -> sum + (if (item.counted) item.value else 0) }

        // trigger LiveData observer
        _tossList.value = list

        // in case if undone Toss finished the game
        if (_isGameOver.value == true) {
            _isGameOver.value = false
            // delete the last saved game
            viewModelScope.launch {
                dartsRepository.deleteLastSavedGame()
            }
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

    //endregion

    //region Functions for [TossFragment]

    /** Called when User tapped on number section in [TossFragment]. */
    fun onSectionTapped(section: Int) {
        val temp = Array(23) { false }
        if (_numberSectorChosen.value?.get(section) == false) {
            temp[section] = true
        }
        _numberSectorChosen.value = temp

        // clicking on 0, Outer Bullseye, Inner Bullseye clears multipliers
        if (section in setOf(0, 21, 22)) {
            _ringChosen.value = Array(2) { false }
        }
    }

    /** Called when User tapped on Multiplier in [TossFragment]. */
    fun onRingTapped(ring: Int) {
        val temp = Array(2) { false }
        if (_ringChosen.value?.get(ring) == false) {
            temp[ring] = true
        }
        _ringChosen.value = temp

        // clear selection on 0, Outer Bullseye (25), Inner Bullseye (50) if any
        val isNonRingSectionChosen = _numberSectorChosen.value?.sliceArray(listOf(0, 21, 22))
            ?.reduce { sum, item -> sum || item } ?: false
        if (isNonRingSectionChosen) {
            _numberSectorChosen.value = Array(23) { false }
        }
    }

    /** Called when User tapped ADD button in [TossFragment]. */
    fun onAddTapped() {
        if (!validateThrow()) {
            // inform user that the trow is invalid
            viewModelScope.launch {
                eventChannel.send(Event.ShowSnackBar(R.string.invalid_trow_message))
            }
            return
        }
        // throw is valid
        val sectionIndex = _numberSectorChosen.value?.indexOf(true) ?: 0
        val ringIndex = (_ringChosen.value?.indexOf(true) ?: -1) + 1
        val newToss =
            Toss(0, false, Toss.Section.values()[sectionIndex], Toss.Ring.values()[ringIndex])
        viewModelScope.launch {
            eventChannel.send(Event.NavigateBackToScoreScreen)
        }

        addNewToss(newToss)

        // reset TossFragment variables
        _numberSectorChosen.value = Array(23) { false }
        _ringChosen.value = Array(2) { false }
    }

    /** Checks if user entered a valid throw. */
    private fun validateThrow(): Boolean {
        // at least one number section chosen
        val trueQuantity = _numberSectorChosen.value?.fold(0) { sum, item ->
            sum + if (item) 1 else 0
        }
        if (trueQuantity != 1) return false

        // if number is 0, Outer Bullseye or Inner Bullseye then x2 and x3 must not be selected
        val isNonRingSectionChosen = _numberSectorChosen.value?.sliceArray(listOf(0, 21, 22))
            ?.reduce { sum, item -> sum || item } ?: false
        val isRingChosen = _ringChosen.value?.reduce { sum, item -> sum || item } ?: false
        if (isNonRingSectionChosen && isRingChosen) {
            return false
        }

        return true
    }
    //endregion

}