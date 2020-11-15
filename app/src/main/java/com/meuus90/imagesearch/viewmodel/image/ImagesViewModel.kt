package com.meuus90.imagesearch.viewmodel.image

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.meuus90.imagesearch.base.arch.util.livedata.LiveEvent
import com.meuus90.imagesearch.base.common.util.customDebounce
import com.meuus90.imagesearch.model.data.source.repository.image.ImagesRepository
import com.meuus90.imagesearch.model.schema.image.ImageRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImagesViewModel
@Inject
constructor(private val repository: ImagesRepository) : ViewModel() {
    val org = MutableLiveData<ImageRequest>()
    lateinit var schemaLiveData: LiveEvent<ImageRequest>

    init {
        viewModelScope.launch {
            schemaLiveData = LiveEvent()
            org.asFlow()
                .customDebounce(500L)
                .collect {
                    postRequestSchema(it)
                }
        }
    }

    @ExperimentalCoroutinesApi
    val images = schemaLiveData.asFlow()
        .distinctUntilChangedBy {
            it.hashCode()
        }
        .flatMapLatest {
            repository.execute(it)
        }

    fun postRequestSchema(requestSchema: ImageRequest) {
        schemaLiveData.value = requestSchema
    }

    fun postRequestSchemaWithDebounce(requestSchema: ImageRequest) {
        org.value = requestSchema
    }
}