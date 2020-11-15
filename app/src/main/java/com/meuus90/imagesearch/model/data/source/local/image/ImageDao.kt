package com.meuus90.imagesearch.model.data.source.local.image

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.meuus90.imagesearch.model.data.source.local.BaseDao
import com.meuus90.imagesearch.model.schema.image.ImageDoc

@Dao
interface ImageDao : BaseDao<ImageDoc> {
    @Query("SELECT * FROM Images")
    fun getImagePagingSource(): PagingSource<Int, ImageDoc>

    @Query("SELECT * FROM Images WHERE collection LIKE :filter")
    fun getImagePagingSource(filter: String): PagingSource<Int, ImageDoc>

    @Query("SELECT * FROM Images")
    fun getImages(): List<ImageDoc>

    @Query("DELETE FROM Images")
    suspend fun clear()
}