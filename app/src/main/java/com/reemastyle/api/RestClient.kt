package com.reemastyle.api

import java.nio.file.WatchEvent


object RestClient : BaseRestClient() {
    private lateinit var restApi: WebService

    init {
        create()
    }

    private fun create() {
        val retrofit = createClient()
        restApi = retrofit.create(WebService::class.java)
    }

    fun recreate(): RestClient {
        create()
        return this
    }

    fun get(): WebService {
        recreate()
        return restApi
    }
}