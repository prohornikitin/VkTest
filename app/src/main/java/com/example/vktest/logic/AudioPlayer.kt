package com.example.vktest.logic

import java.nio.file.Path

interface AudioPlayer {

    fun changePlayingFile(path: Path): Boolean
    fun pause()
    fun resume()

    fun setOnCompletionListener(f: ()->Unit)
}