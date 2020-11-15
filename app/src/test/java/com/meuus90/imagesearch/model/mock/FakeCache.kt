package com.meuus90.imagesearch.model.mock

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.meuus90.imagesearch.model.data.source.local.Cache
import com.meuus90.imagesearch.model.data.source.local.image.ImageDao
import org.mockito.Mockito

class FakeCache : Cache() {
    override fun imageDao(): ImageDao {
        return FakeRoom()
    }

    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper {
        return Mockito.mock(SupportSQLiteOpenHelper::class.java) as SupportSQLiteOpenHelper
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        return Mockito.mock(InvalidationTracker::class.java) as InvalidationTracker
    }

    override fun clearAllTables() {
    }
}