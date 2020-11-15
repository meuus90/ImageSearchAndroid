package com.meuus90.imagesearch.model.mock

import com.meuus90.imagesearch.model.data.source.api.DaumAPI
import com.meuus90.imagesearch.model.schema.image.ImageResponse

class FakeDaumAPI : DaumAPI {
    override suspend fun getImageList(
        query: String,
        sort: String?,
        size: Int?,
        page: Int?
    ): ImageResponse {
        return FakeModel.mockResponseModel
    }
}