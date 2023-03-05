package com.example.vktest.data

import androidx.room.TypeConverter
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.io.path.Path

class DbTypeConverters {
    @TypeConverter
    fun toDateTime(epoch: Long) = LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC)

    @TypeConverter
    fun fromDateTime(dateTime: LocalDateTime) = dateTime.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun fromPath(path: Path) = path.toString()

    @TypeConverter
    fun toPath(str: String) = Path(str)
}