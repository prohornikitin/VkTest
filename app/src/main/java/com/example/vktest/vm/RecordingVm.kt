package com.example.vktest.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vktest.logic.AudioRecorder
import com.example.vktest.logic.NotesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Path
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.io.path.deleteIfExists


private const val LOG_TAG = "RecordingVm"

@HiltViewModel
class RecordingVm @Inject constructor(
    private val recorder: AudioRecorder,
    private val repo: NotesRepo
) : ViewModel() {
    enum class RecordingState {
        PERMISSION_REQUEST,
        ERROR_UNEXPECTED,
        RECORDING,
        NAMING,
        IDLE,
        ERROR_CLICK_TOO_FAST
    }

    private val state_ = MutableLiveData(RecordingState.IDLE)
    val state: LiveData<RecordingState> = state_
    private val disableButton_ = MutableLiveData(true)
    val enabledButton: LiveData<Boolean> = disableButton_

    private var lastRecordPath: Path? = null
    private var lastRecordTimestamp: LocalDateTime? = null
    private var onPermissionGrantedCallback: (() -> Unit)? = null


    fun toggleRecording() {
        if(state.value == RecordingState.RECORDING) {
            stopRecording()
        } else {
            onPermissionGrantedCallback = ::startRecording
            state_.value = RecordingState.PERMISSION_REQUEST
        }
    }

    fun onPermissionGranted() {
        if(state.value != RecordingState.PERMISSION_REQUEST) {
            Log.e(LOG_TAG, "permissions was not requested but was granted :)")
            return
        }
        onPermissionGrantedCallback?.invoke()
        onPermissionGrantedCallback = null
    }

    private fun startRecording() {
        if(state.value == RecordingState.RECORDING) {
            Log.e(LOG_TAG, "tried to startRecording() while it was already in RECORDING state")
            state_.value = RecordingState.ERROR_UNEXPECTED
            return
        }
        state_.value = RecordingState.RECORDING
        val result = recorder.start()
        if(result.isFailure) {
            state_.postValue(RecordingState.ERROR_UNEXPECTED)
        }
    }

    private fun stopRecording() {
        if(state.value != RecordingState.RECORDING) {
            Log.e(LOG_TAG, "tried to stopRecording() without startRecording()")
            state_.value = RecordingState.ERROR_UNEXPECTED
            return
        }
        val result = recorder.stop()
        result.onFailure {
            state_.postValue(when(it) {
                is java.lang.RuntimeException -> RecordingState.ERROR_CLICK_TOO_FAST
                else -> RecordingState.ERROR_UNEXPECTED
            })
            return
        }
        lastRecordPath = result.getOrThrow().toPath()
        lastRecordTimestamp = LocalDateTime.now()
        state_.postValue(RecordingState.NAMING)
    }

    fun namingComplete(name: String) {
        if(lastRecordPath == null) {
            Log.e(LOG_TAG, "record path is null")
            state_.value = RecordingState.ERROR_UNEXPECTED
            return
        }

        if(lastRecordTimestamp == null) {
            Log.e(LOG_TAG, "record timestamp is null. ")
            lastRecordTimestamp = LocalDateTime.now()
        }
        viewModelScope.launch(Dispatchers.IO) {
            repo.addFromTemporaryFile(name, lastRecordTimestamp!!, lastRecordPath!!)
            state_.postValue(RecordingState.IDLE)
        }
    }

    fun namingCanceled() {
        lastRecordPath?.deleteIfExists()
    }


}