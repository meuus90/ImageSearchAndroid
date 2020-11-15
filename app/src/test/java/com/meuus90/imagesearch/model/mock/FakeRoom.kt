package com.meuus90.imagesearch.model.mock

import androidx.paging.PagingSource
import com.meuus90.imagesearch.model.data.source.local.image.ImageDao
import com.meuus90.imagesearch.model.schema.image.ImageDoc
import org.mockito.Mockito

class FakeRoom : ImageDao {
    override fun getImagePagingSource(): PagingSource<Int, ImageDoc> {
        return mockPagedList()
    }

    override fun getImagePagingSource(filter: String): PagingSource<Int, ImageDoc> {
        return mockPagedList()
    }

    override fun getImages(): List<ImageDoc> {
        return listOf()
    }

    override suspend fun clear() {
    }

    override suspend fun insert(vararg obj: ImageDoc) {
    }

    override suspend fun insert(obj: ImageDoc) {
    }

    override suspend fun insert(obj: MutableList<ImageDoc>) {
    }

    override suspend fun insert(obj: ArrayList<ImageDoc>) {
    }

    override suspend fun delete(obj: ImageDoc) {
    }

    override suspend fun update(vararg obj: ImageDoc) {
    }

    override suspend fun update(obj: ImageDoc) {
    }

    override suspend fun update(obj: MutableList<ImageDoc>) {
    }

    fun mockPagedList(): PagingSource<Int, ImageDoc> {
        val pagingSource = Mockito.mock(PagingSource::class.java) as PagingSource<Int, ImageDoc>
//        Mockito.`when`(pagedList.get(ArgumentMatchers.anyInt())).then { invocation ->
//            val index = invocation.arguments.first() as Int
//            list[index]
//        }
//        Mockito.`when`(pagedList.size).thenReturn(list.size)
        return pagingSource
    }
}