package com.example.vktest.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {
    @Query("SELECT * FROM notes")
    fun getAll(): LiveData<List<Note>>

    @Insert
    fun insertNew(note: Note)
}



