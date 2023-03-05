package com.example.vktest.logic

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vktest.data.Note
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.path.*

@Singleton
class NotesRepo @Inject constructor(@ApplicationContext val appContext: Context) {
    private val notesMutable = mutableListOf(
        Note("Поход к адвокату", LocalDateTime.of(2023, 3, 2, 12, 51), Path("file1")),
        Note("Разговор с Иваном", LocalDateTime.of(2022, 2, 14, 15, 32), Path("file2"))
    ) //TODO: create database
    private val notes_ = MutableLiveData<List<Note>>(notesMutable.toList())
    val notes: LiveData<List<Note>> = notes_

    fun addFromTemporaryFile(title: String, recordingTimestamp: LocalDateTime, tempFile: Path) {
        val permanentPath = appContext.filesDir.toPath() / generatePermanentFileName()
        tempFile.copyTo(permanentPath)
        tempFile.deleteExisting()
        add(Note(title, recordingTimestamp, permanentPath))
    }

    private fun generatePermanentFileName() : String {
        val timeStamp: String = SimpleDateFormat("yyyy_MM_dd_HH-mm-ss", Locale.getDefault()).format(Date())
        var fileName = "audio_$timeStamp"
        while((appContext.filesDir.toPath() / fileName).exists()) {
            fileName += "_"
        }
        return fileName
    }

    fun add(note: Note) {
        notesMutable.add(note)
        notes_.postValue(notesMutable.toList())
    }
}