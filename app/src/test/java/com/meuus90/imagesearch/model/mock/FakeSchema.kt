package com.meuus90.imagesearch.model.mock

import com.meuus90.imagesearch.model.schema.image.ImageRequest

object FakeSchema {
    val mockImageSchema = ImageRequest(
        query = "test",
        sort = ImageRequest.SORT_ACCURACY,
        size = 50,
        collection = null
    )

    val mockImageSchema0 = ImageRequest("test0", null, null, null)
    val mockImageSchema1 = ImageRequest("test1", null, null, null)
    val mockImageSchema2 = ImageRequest("test2", null, null, null)
}