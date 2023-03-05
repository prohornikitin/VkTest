package com.example.vktest.dependencyInjection

import com.example.vktest.logic.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirstModule /**Название временное, нужно сменить**/ {
    @Binds
    abstract fun bindAudioPlayer(media: AudioPlayerImpl): AudioPlayer

    @Binds
    abstract fun bindAudioRecorder(media: AudioRecorderImpl): AudioRecorder
}