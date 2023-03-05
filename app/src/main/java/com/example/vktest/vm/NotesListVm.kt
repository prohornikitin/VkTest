package com.example.vktest.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vktest.data.Note
import com.example.vktest.logic.AudioPlayer
import com.example.vktest.logic.NotesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotesListVm @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val repo: NotesRepo
) : ViewModel() {
    enum class Error {
        CORRUPTED_AUDIO,
        UNEXPECTED_ERROR
    }

    var deleteMode = false

    private val error_ = MutableLiveData<Error?>(null)
    val error: LiveData<Error?> = error_

    private val notes_ = MutableLiveData(emptyList<NoteItemVm>())

    private val repoObserver = { list: List<Note> ->
        notes_.value = list.map {
            return@map NoteItemVm(it)
        }
    }

    init {
        repo.notes.observeForever(repoObserver)
        audioPlayer.setOnCompletionListener {
            pause()
        }
    }
    override fun onCleared() {
        repo.notes.removeObserver(repoObserver)
        super.onCleared()
    }

    val notes: LiveData<List<NoteItemVm>> = notes_

    private var currentRecordPosition: Int? = null

    fun pause() {
        viewModelScope.launch(Dispatchers.IO) {
            audioPlayer.pause()
        }
        viewModelScope.launch(Dispatchers.Main) {
            notes_.value = notes.value!!.mapIndexed { i: Int, noteItemVm: NoteItemVm ->
                if (i == currentRecordPosition) {
                    return@mapIndexed noteItemVm.deselect()
                }
                return@mapIndexed noteItemVm
            }
        }
    }

    fun play(note: NoteItemVm, position: Int) {
        if (currentRecordPosition == position) {
            viewModelScope.launch(Dispatchers.IO) {
                audioPlayer.resume()
            }
            notes_.value = notes.value!!.mapIndexed { i: Int, noteItemVm: NoteItemVm ->
                if (i == currentRecordPosition) {
                    return@mapIndexed noteItemVm.select()
                }
                return@mapIndexed noteItemVm
            }
        }
        val prevRecordPosition = currentRecordPosition
        currentRecordPosition = position
        notes_.value = notes.value!!.mapIndexed { i: Int, noteItemVm: NoteItemVm ->
            if (i == currentRecordPosition) {
                return@mapIndexed noteItemVm.select()
            }
            if (i == prevRecordPosition) {
                return@mapIndexed noteItemVm.deselect()
            }
            return@mapIndexed noteItemVm
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (!audioPlayer.changePlayingFile(note.audioSource)) {
                error_.postValue(Error.CORRUPTED_AUDIO)
                pause()
            }
        }
    }

    fun itemClick(position: Int) {
        if(!deleteMode) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(notes.value!![position].note)
        }
    }
}