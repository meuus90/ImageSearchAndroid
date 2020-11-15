package com.meuus90.imagesearch.base.common.util

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow


fun <T> Flow<T>.customDebounce(waitMillis: Long) = flow {
    coroutineScope {
        val context = coroutineContext
        var delayPost: Deferred<Unit>? = null
        collect {
            delayPost?.cancel()
            delayPost = async(Dispatchers.Default) {
                delay(waitMillis)
                withContext(context) {
                    emit(it)
                }
            }
        }
    }
}

fun <T> Flow<T>.customThrottleFirst(waitMillis: Long) = flow {
    coroutineScope {
        val context = coroutineContext
        var delayPost: Deferred<Unit>? = null

        collect {
            if (delayPost?.isActive != true)
                delayPost = async(Dispatchers.Default) {
                    delay(waitMillis)
                    withContext(context) {
                        emit(it)
                    }
                }
        }
    }
}

fun <T> Flow<T>.customThrottleLast(waitMillis: Long) = flow {
    coroutineScope {
        val context = coroutineContext
        var delayPost: Deferred<Unit>? = null

        var new: T

        collect {
            new = it
            if (delayPost?.isActive != true)
                delayPost = async(Dispatchers.Default) {
                    delay(waitMillis)
                    withContext(context) {
                        emit(new)
                    }
                }
        }
    }
}