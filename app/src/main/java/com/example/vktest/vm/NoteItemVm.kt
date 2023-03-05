package com.example.vktest.vm

import com.example.vktest.data.Note
import java.nio.file.Path
import java.time.LocalDateTime

class NoteItemVm(private val note: Note, val selected: Boolean = false) {
    val title: String
        get() = note.title
    val recordingTimestamp: LocalDateTime
        get() = note.timestamp
    val audioSource: Path
        get() = note.audioSource

    fun deselect(): NoteItemVm {
        return NoteItemVm(note, false)
    }

    fun select(): NoteItemVm{
        return NoteItemVm(note, true)
    }
}
