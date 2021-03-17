package com.beletskiy.dartstrainingcalculator.fragments.scores

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.utils.TAG

class ScoresViewModel : ViewModel() {

    private val _tossList = MutableLiveData<ArrayList<Toss>>()
    val tossList: LiveData<ArrayList<Toss>>
        get() = _tossList

    init {
        Log.i(TAG, "ScoresViewModel.init")
        _tossList.value = arrayListOf(
            Toss(Toss.Section.SEVENTEEN, Toss.Ring.X3),
            Toss(Toss.Section.FIVE, Toss.Ring.X1)
        )
    }

    fun onNewTossCreated(newToss: Toss) {
        _tossList.value?.add(newToss)
        Log.i(TAG, "ScoresViewModel : _tossList.size = ${_tossList.value?.size}")
        _tossList.value = _tossList.value
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "ScoresViewModel.onCleared !!!!!!!!!")
    }
}