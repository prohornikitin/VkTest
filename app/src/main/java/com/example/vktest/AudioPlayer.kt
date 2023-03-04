package com.example.vktest

import java.nio.file.Path

interface AudioPlayer {
    suspend fun changePlayingFile(path: Path): Boolean
    suspend fun pause()
    suspend fun resume()
}