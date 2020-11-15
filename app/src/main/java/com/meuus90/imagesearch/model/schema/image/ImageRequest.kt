package com.meuus90.imagesearch.model.schema.image

data class ImageRequest(
    var query: String,
    var sort: String?,
    val size: Int?,
    var collection: String?
) {

    companion object {
        const val SORT_ACCURACY = "accuracy"
        const val SORT_RECENCY = "recency"
    }

    fun setQueryStr(searchStr: String): Boolean {
        val old = query
        query = searchStr

        return old != query
    }

    fun setSortType(sort: String): Boolean {
        val old = this.sort
        this.sort = sort

        return old != sort
    }

    fun setCollection(collection: String?): Boolean {
        val old = this.collection
        this.collection = collection

        return old != collection
    }
}