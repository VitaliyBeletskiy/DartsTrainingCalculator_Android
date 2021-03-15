package com.beletskiy.dartstrainingcalculator.fragments.toss

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beletskiy.dartstrainingcalculator.R
import com.beletskiy.dartstrainingcalculator.utils.TAG
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TossViewModel : ViewModel() {

    // for SingleLiveEvent
    sealed class Event {
        object NavigateToMainScreen: Event()
        data class ShowSnackBar(val stringId: Int): Event()
    }

    // for SingleLiveEvent
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    // contains values for number sections 1-20 as well as 0, Outer Bullseye, Inner Bullseye
    private val _numberSectorChosen = MutableLiveData(Array(23) { false })
    val numberSectorChosen: LiveData<Array<Boolean>>
        get() = _numberSectorChosen

    // contains values for x2 and x3 rings
    private val _ringChosen = MutableLiveData(Array(2) { false })
    val ringChosen: LiveData<Array<Boolean>>
        get() = _ringChosen

    /// called when User tapped on number section
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

    /// called when User tapped on Multiplier
    fun onRingTapped(ring: Int) {
        val temp = Array(2) { false }
        if (_ringChosen.value?.get(ring) == false) {
            temp[ring] = true
        }
        _ringChosen.value = temp
    }

    /// called when User tapped ADD button
    fun onAddTapped() {
        if (!validateThrow()) {
            // inform user that the trow is invalid
            viewModelScope.launch {
                eventChannel.send(Event.ShowSnackBar(R.string.invalid_trow_message))
            }
            return
        }
        // TODO: proceed with a valid throw
        Log.i(TAG, "throw is valid")
    }

    /// checks if user entered a valid throw
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

}
