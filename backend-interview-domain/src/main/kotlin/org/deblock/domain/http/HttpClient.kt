package org.deblock.domain.http

interface HttpClient {

    suspend fun <T> sendGet(
        endpoint: String,
        responseType: Class<T>,
        queryParams: Map<String, String> = emptyMap()): T
}