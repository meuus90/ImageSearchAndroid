package com.meuus90.imagesearch.model.data.source.repository.image

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.meuus90.imagesearch.base.constant.AppConfig
import com.meuus90.imagesearch.model.data.source.api.DaumAPI
import com.meuus90.imagesearch.model.data.source.local.Cache
import com.meuus90.imagesearch.model.paging.PagingDataInterface
import com.meuus90.imagesearch.model.paging.image.ImagesPageKeyedMediator
import com.meuus90.imagesearch.model.schema.image.ImageDoc
import com.meuus90.imagesearch.model.schema.image.ImageRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImagesRepository
@Inject
constructor(private val db: Cache, private val daumAPI: DaumAPI) :
    PagingDataInterface<ImageRequest, Flow<PagingData<ImageDoc>>> {

    private var collection: String? = null
    override fun execute(requestSchema: ImageRequest): Flow<PagingData<ImageDoc>> {
        collection = requestSchema.collection

        return Pager(
            config = PagingConfig(
                initialLoadSize = AppConfig.localInitialLoadSize,
                pageSize = AppConfig.localPagingSize,
                prefetchDistance = AppConfig.localPrefetchDistance,
                enablePlaceholders = false
            ),
            remoteMediator = ImagesPageKeyedMediator(
                db,
                daumAPI,
                requestSchema
            )
        ) {
            if (collection == null)
                db.imageDao().getImagePagingSource()
            else
                db.imageDao().getImagePagingSource(requestSchema.collection ?: "")
        }.flow
    }
}
