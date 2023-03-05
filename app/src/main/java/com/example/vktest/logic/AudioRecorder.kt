package com.example.vktest.logic

import java.io.File

interface AudioRecorder {
    fun start(): Result<Unit>
    fun stop(): Result<File>
}