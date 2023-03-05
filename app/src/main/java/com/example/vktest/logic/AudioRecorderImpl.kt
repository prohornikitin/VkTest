package com.example.vktest.logic

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

private const val LOG_TAG = "AudioRecorderImpl"

class AudioRecorderImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : AudioRecorder {
    private var recorder: MediaRecorder? = null

    private var output: File? = null


    override fun start() : Result<Unit>{
        recorder?.release()
        recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(appContext)
        } else {
            MediaRecorder()
        }

        output = generateOutputFile()

        recorder!!.apply {
            try {
                setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            } catch (e: java.lang.RuntimeException) {
                e.printStackTrace()
                return Result.failure(e)
            }
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(output)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
                if(output!!.exists()) {
                    output!!.delete()
                }
                return Result.failure(e)
            }

            start()
        }
        return Result.success(Unit)
    }

    private fun generateOutputFile() : File {
        return File.createTempFile("audio_tmp", ".acc", appContext.cacheDir)
    }

    override fun stop() : Result<File> {
        try {
            if (output == null) {
                return Result.failure(java.lang.IllegalStateException())
            }
            recorder?.stop()
            val res = Result.success(output!!)
            output = null
            return res
        } catch (e: RuntimeException) {
            if(output!!.exists()) {
                output!!.delete()
            }
            return Result.failure(e)
        }
    }
}