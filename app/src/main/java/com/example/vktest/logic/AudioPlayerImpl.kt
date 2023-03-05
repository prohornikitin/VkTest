package com.example.vktest.logic

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.div
import kotlin.io.path.notExists

private const val LOG_TAG = "AudioPlayerImpl"

class AudioPlayerImpl @Inject constructor(@ApplicationContext val appContext: Context) :
    AudioPlayer {
    var media: MediaPlayer? = null
    var onCompletion: ()->Unit = {}
    override fun changePlayingFile(path: Path) : Boolean {
        if(media != null) {
            if(media!!.isPlaying) {
                media?.stop()
            }
            media!!.release()
        }


        val fullPath = (appContext.filesDir.toPath() / path)
        if(fullPath.notExists()) {
            Log.e(LOG_TAG, "path $fullPath does not exists")
            return false
        }
        val uri = Uri.fromFile(fullPath.toFile())
        media = MediaPlayer.create(appContext, uri)
        if(media == null) {
            return false
        }
        media!!.setOnCompletionListener { onCompletion() }
        media!!.start()
        return true
    }

    override fun pause() {
        media?.pause()
    }

    override fun resume() {
        media?.start()
    }

    override fun setOnCompletionListener(f: () -> Unit) {
        media?.setOnCompletionListener { f() }
        onCompletion = f
    }
}