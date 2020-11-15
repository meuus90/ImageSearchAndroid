package com.meuus90.imagesearch.model.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meuus90.imagesearch.base.constant.AppConfig
import com.meuus90.imagesearch.model.data.source.local.image.ImageDao
import com.meuus90.imagesearch.model.schema.image.ImageDoc

@Database(
    entities = [ImageDoc::class],
    exportSchema = false,
    version = AppConfig.roomVersionCode
)
abstract class Cache : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}