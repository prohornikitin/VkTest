package com.example.vktest.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.nio.file.Path
import java.time.LocalDateTime

@Entity(tableName = "notes")
data class Note(
    val title: String,
    @PrimaryKey val timestamp: LocalDateTime,
    val audioSource: Path,/**relative to the files directory of the app**/
)
