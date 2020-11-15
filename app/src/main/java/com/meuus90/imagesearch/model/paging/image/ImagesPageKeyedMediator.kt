package com.meuus90.imagesearch.model.paging.image

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.meuus90.imagesearch.model.data.source.api.DaumAPI
import com.meuus90.imagesearch.model.data.source.local.Cache
import com.meuus90.imagesearch.model.data.source.local.image.ImageDao
import com.meuus90.imagesearch.model.schema.image.ImageDoc
import com.meuus90.imagesearch.model.schema.image.ImageRequest
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ImagesPageKeyedMediator(
    private val db: Cache,
    private val daumAPI: DaumAPI,
    private val imageSchema: ImageRequest
) : RemoteMediator<Int, ImageDoc>() {
    private val postDao: ImageDao = db.imageDao()

    private var loadKey = 1
    private var isEnd = false

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageDoc>
    ): MediatorResult {
        try {
            when (loadType) {
                LoadType.REFRESH -> {
                    loadKey = 1
                    isEnd = false

                    db.withTransaction {
                        postDao.clear()
                    }
                }
                LoadType.PREPEND -> {
                }
                LoadType.APPEND -> {
                    if (!isEnd) {
                        val response = daumAPI.getImageList(
                            query = imageSchema.query,
                            sort = imageSchema.sort,
                            size = imageSchema.size,
                            page = loadKey
                        )
                        Timber.e("response ${response.meta}")

                        if (response.meta.total_count == 0) {
                            val e = EmptyResultException()
                            Timber.e(e)
                            return MediatorResult.Error(e)
                        }

                        db.withTransaction {
                            postDao.insert(response.documents)
                        }

                        isEnd = response.meta.is_end || loadKey >= 50

                        if (!isEnd)
                            loadKey++
                    }
                }
            }
            Timber.e("load end $loadType, $loadKey, $isEnd")

            return MediatorResult.Success(endOfPaginationReached = isEnd)
        } catch (e: IOException) {
            Timber.e(e)
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            Timber.e(e)
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            Timber.e(e)
            return MediatorResult.Error(e)
        }
    }

    class EmptyResultException() : Exception()
}
