package com.meuus90.imagesearch.model.data.source.api

import com.meuus90.imagesearch.model.schema.image.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DaumAPI {
    @GET("v2/search/image")
    suspend fun getImageList(
        @Query("query") query: String,
        @Query("sort") sort: String?,
        @Query("size") size: Int?,
        @Query("page") page: Int?
    ): ImageResponse
}