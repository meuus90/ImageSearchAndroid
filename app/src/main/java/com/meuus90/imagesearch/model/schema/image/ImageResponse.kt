package com.meuus90.imagesearch.model.schema.image

import com.meuus90.imagesearch.model.schema.BaseSchema

data class ImageResponse(
    val meta: Meta,
    val documents: MutableList<ImageDoc>
) : BaseSchema() {

    data class Meta(
        val is_end: Boolean,
        val pageable_count: Int,
        val total_count: Int
    ) : BaseSchema()
}