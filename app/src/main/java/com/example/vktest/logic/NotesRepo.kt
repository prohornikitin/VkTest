package com.example.vktest.logic

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vktest.data.Note
import com.example.vktest.data.NotesDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.path.*

@Singleton
class NotesRepo @Inject constructor(
    @ApplicationContext private val appContext: Context,
    db: NotesDatabase
) {
    private val notesDao = db.dao()
    val notes: LiveData<List<Note>> = notesDao.getAll()

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
        notesDao.insertNew(note)
    }
}