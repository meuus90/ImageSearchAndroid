package com.meuus90.imagesearch.model.mock

import com.meuus90.imagesearch.model.schema.image.ImageDoc
import com.meuus90.imagesearch.model.schema.image.ImageResponse

object FakeModel {
    val mockImageList = mutableListOf(
        ImageDoc(
            databaseId = 0,
            collection = "news",
            thumbnail_url = "https://search2.kakaocdn.net/argon/130x130_85_c/36hQpoTrVZp",
            image_url = "http://t1.daumcdn.net/news/201706/21/kedtv/20170621155930292vyyx.jpg",
            width = 540,
            height = 457,
            display_sitename = "한국경제TV",
            doc_url = "http://v.media.daum.net/v/20170621155930002",
            datetime = "2017-06-21T15:59:30.000+09:00"
        ),
        ImageDoc(
            databaseId = 1,
            collection = "news",
            thumbnail_url = "https://search2.kakaocdn.net/argon/130x130_85_c/36hQpoTrVZp",
            image_url = "http://t1.daumcdn.net/news/201706/21/kedtv/20170621155930292vyyx.jpg",
            width = 540,
            height = 457,
            display_sitename = "한국경제TV",
            doc_url = "http://v.media.daum.net/v/20170621155930002",
            datetime = "2017-06-21T15:59:30.000+09:00"
        ),
        ImageDoc(
            databaseId = 2,
            collection = "news",
            thumbnail_url = "https://search2.kakaocdn.net/argon/130x130_85_c/36hQpoTrVZp",
            image_url = "http://t1.daumcdn.net/news/201706/21/kedtv/20170621155930292vyyx.jpg",
            width = 540,
            height = 457,
            display_sitename = "한국경제TV",
            doc_url = "http://v.media.daum.net/v/20170621155930002",
            datetime = "2017-06-21T15:59:30.000+09:00"
        )
    )

    val mockResponseModel = ImageResponse(
        ImageResponse.Meta(
            is_end = true,
            pageable_count = 9,
            total_count = 10
        ),
        documents = mockImageList
    )

    val mockImageResponseEmptyModel = ImageResponse(
        ImageResponse.Meta(
            is_end = true,
            pageable_count = 9,
            total_count = 10
        ),
        documents = mutableListOf()
    )
}