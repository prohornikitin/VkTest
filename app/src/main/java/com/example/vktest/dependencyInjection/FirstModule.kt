package com.example.vktest.dependencyInjection

import com.example.vktest.AudioPlayer
import com.example.vktest.MediaAudioPlayer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FirstModule /**Название временное, нужно сменить**/ {
    @Binds
    abstract fun bindAudioPlayer(media: MediaAudioPlayer): AudioPlayer
}