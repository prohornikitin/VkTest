package com.example.vktest.data

import java.nio.file.Path
import java.time.LocalDateTime

data class Note(
    val title: String,
    val recordingTimestamp: LocalDateTime,
    val audioSource: Path,/**relative to the files directory of the app**/
)
