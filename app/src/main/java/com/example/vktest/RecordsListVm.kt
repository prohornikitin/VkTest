package com.example.vktest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vktest.data.RecordVm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.io.path.Path


@HiltViewModel
class RecordsListVm @Inject constructor(private val audioPlayer: AudioPlayer) : ViewModel() {
    val records = MutableLiveData<List<RecordVm>>(listOf(
        RecordVm("Поход к адвокату", LocalDateTime.of(2023, 3, 2, 12, 51), Path("file1")),
        RecordVm("Разговор с Иваном", LocalDateTime.of(2022, 2, 14, 15, 32), Path("file2"))
    ))

    private val errorStringId_ = MutableLiveData<Int>(null)
    val errorStringId: LiveData<Int> = errorStringId_

    private var playingPosition: Int? = null

    fun pause(record: RecordVm, position: Int) = viewModelScope.launch(Dispatchers.IO) {
        playingPosition = null
        audioPlayer.pause()
    }

    fun play(record: RecordVm, position: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (playingPosition == position) {
            audioPlayer.resume()
        } else {
            Log.d("list_before_change", records.value!!.toString())
            records.postValue(records.value!!.mapIndexed { i: Int, recordVm: RecordVm ->
                if(i == playingPosition) {
                    return@mapIndexed recordVm.copy(selected = false)
                }
                if(i == position) {
                    return@mapIndexed recordVm.copy(selected = true)
                }
                return@mapIndexed recordVm
            })
            Log.d("list_after_change", records.value!!.toString())
            playingPosition = position
            if(!audioPlayer.changePlayingFile(record.audioSource)) {
                errorStringId_.postValue(R.string.error_corrupted_audio)
            }
        }
    }

}