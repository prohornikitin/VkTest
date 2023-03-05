package com.example.vktest.logic

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.div

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


        val fullPath = Uri.fromFile((appContext.filesDir.toPath() / path).toFile())
        media = MediaPlayer.create(appContext, fullPath)
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