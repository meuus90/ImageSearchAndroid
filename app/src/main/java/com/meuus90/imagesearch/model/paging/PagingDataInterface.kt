package com.meuus90.imagesearch.model.paging


interface PagingDataInterface<Request, Response> {
    fun execute(requestSchema: Request): Response
}