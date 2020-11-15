package com.meuus90.imagesearch.viewmodel.image

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.meuus90.imagesearch.base.common.util.customThrottleLast
import com.meuus90.imagesearch.model.data.source.local.Cache
import com.meuus90.imagesearch.model.schema.image.CollectionItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalViewModel
@Inject
constructor(private val db: Cache) : ViewModel() {

    fun clearCache() = GlobalScope.launch(Dispatchers.IO) {
        db.imageDao().clear()
    }

    val org = MutableLiveData<ArrayList<CollectionItem>>()

    @ExperimentalCoroutinesApi
    val collections = org.asFlow()
        .customThrottleLast(300L)

    fun getCollectionList(collection: String?) {
        GlobalScope.launch(Dispatchers.IO) {
            val list = arrayListOf(
                CollectionItem(null, collection == null)
            )

            list.addAll(
                db.imageDao().getImages()
                    .map { img ->
                        CollectionItem(img.collection, img.collection == collection)
                    }.distinct().sortedBy {
                        it.collection
                    }
            )

            org.postValue(list)
        }
    }
}