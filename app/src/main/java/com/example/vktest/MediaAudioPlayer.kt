package com.example.vktest

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.system.ErrnoException
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.div

class MediaAudioPlayer @Inject constructor(@ApplicationContext val appContext: Context) : AudioPlayer{
    var media: MediaPlayer? = null


    override suspend fun changePlayingFile(path: Path) : Boolean {
//        if(media != null) {
//            if(media!!.isPlaying) {
//                media?.stop()
//            }
//            media!!.release()
//        }
//
//
//        val fullPath = Uri.fromFile((appContext.filesDir.toPath() / path).toFile())
//        media = MediaPlayer.create(appContext, fullPath)
//        if(media == null) {
            return false
//        }
//        media!!.start()
//        return true
    }

    override suspend fun pause() {
        media?.pause()
    }

    override suspend fun resume() {
        media?.start()
    }
}