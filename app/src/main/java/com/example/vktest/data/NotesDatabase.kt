package com.example.vktest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Note::class], version = 1)
@TypeConverters(DbTypeConverters::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun dao(): NotesDao
}