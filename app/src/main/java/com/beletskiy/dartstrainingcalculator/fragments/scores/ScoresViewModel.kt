package com.beletskiy.dartstrainingcalculator.fragments.scores

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beletskiy.dartstrainingcalculator.data.Toss
import com.beletskiy.dartstrainingcalculator.utils.TAG

class ScoresViewModel : ViewModel() {

    private val _tossList = MutableLiveData<ArrayList<Toss>>(ArrayList())
    val tossList: LiveData<ArrayList<Toss>>
        get() = _tossList

    fun onNewTossCreated(newToss: Toss) {
        _tossList.value?.add(newToss)
        _tossList.value = _tossList.value
    }

}