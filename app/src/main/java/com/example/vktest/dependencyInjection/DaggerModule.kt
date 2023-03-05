package com.example.vktest.dependencyInjection

import android.content.Context
import androidx.room.Room
import com.example.vktest.data.NotesDatabase
import com.example.vktest.logic.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DaggerModule /**Название временное, нужно сменить**/ {
    @Binds
    abstract fun bindAudioPlayer(media: AudioPlayerImpl): AudioPlayer

    @Binds
    abstract fun bindAudioRecorder(media: AudioRecorderImpl): AudioRecorder

    companion object {
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context
        ): NotesDatabase {
            return Room.databaseBuilder(
                context,
                NotesDatabase::class.java,
                "id"
            ).build()
        }
    }
}