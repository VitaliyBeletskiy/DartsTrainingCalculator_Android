package com.beletskiy.dartstrainingcalculator.fragments.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.beletskiy.dartstrainingcalculator.data.Toss

class ScoreViewModel(private var game: Int) : ViewModel() {

    // contains all throws
    private val _tossList = MutableLiveData<ArrayList<Toss>>(ArrayList())
    val tossList: LiveData<ArrayList<Toss>>
        get() = _tossList

    private var currentScore: Int = 0
    private var score: Int = 0

    fun restartGame() {
        _tossList.value = ArrayList()

    }

    // when User added e new throw
    fun onNewTossCreated(newToss: Toss) {
        newToss.id = _tossList.value?.size ?: 1
        _tossList.value?.add(newToss)
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