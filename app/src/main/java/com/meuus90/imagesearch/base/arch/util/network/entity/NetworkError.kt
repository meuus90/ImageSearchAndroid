package com.meuus90.imagesearch.base.arch.util.network.entity

data class NetworkError(val errorType: String?, val message: String = ERROR_DEFAULT) {

    fun isMissingParameter() = errorType == "MissingParameter"

    companion object {
        const val ERROR_DEFAULT = "An unexpected error has occurred"
        const val ERROR_SERVICE_UNAVAILABLE = 503
    }
}