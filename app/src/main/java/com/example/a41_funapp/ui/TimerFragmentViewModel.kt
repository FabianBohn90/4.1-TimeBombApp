package com.example.a41_funapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.NumberFormatException
import java.util.concurrent.TimeUnit

class TimerFragmentViewModel() : ViewModel() {
    private var timeInMillis: Long = 1L

    // eine Varbiable namens currentJob
    private lateinit var currentJob: Job

    // eine Variable delay
    var delay: Long = 1000

    private var _stringTime = MutableLiveData<String>("")
    val stringTime: LiveData<String>
        get() = _stringTime

    private var _countDownInitiated: Boolean = false
    val countDownInitiated: Boolean
        get() = _countDownInitiated

    private var _countDownActive = MutableLiveData<Boolean>(false)
    val countDownActive: LiveData<Boolean>
        get() = _countDownActive

    fun countDownTime(timeString: String) {
        _stringTime.value = timeString
        convertTimeToMillis(timeString)
        _countDownInitiated = true
        currentJob = viewModelScope.launch(Dispatchers.Main) {
            _countDownActive.postValue(true)
            while (timeInMillis > 0) {
                delay(delay)
                timeInMillis -= 1000
                convertTimeToString(timeInMillis)
            }
            _countDownActive.postValue(false)
            delay = 1000
        }
    }

    fun stopCurrentJob() {
        currentJob.cancel()
    }

    fun fastRunCurrentJob() {
        delay = 10
    }

    private fun convertTimeToString(millis: Long) {
        _stringTime.value = String.format(
            "%02d",
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    millis
                )
            )
        )
    }

    private fun convertTimeToMillis(timeString: String) {
        var seconds = try {
            timeString.toInt()
        } catch (ex: NumberFormatException) {
            0
        }

        timeInMillis = (seconds * 1000L)

        println("Millis: $timeInMillis")
    }

    fun resetCountDown() {
        _countDownInitiated = false
    }
}
