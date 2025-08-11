package org.deblock.org.deblock.app.http

import kotlinx.coroutines.reactive.awaitSingle
import org.deblock.domain.http.HttpClient
import org.springframework.util.MultiValueMap.fromSingleValue
import org.springframework.web.reactive.function.client.WebClient

class WebFluxHttpClient(
    private val webClient: WebClient,
) : HttpClient {

    override suspend fun <T> sendGet(
        endpoint: String,
        responseType: Class<T>,
        queryParams: Map<String, String>,
    ): T = webClient.get()
        .uri {
            it.path(endpoint)
                .queryParams(fromSingleValue(queryParams))
                .build()
        }
        .retrieve()
        .bodyToMono(responseType)
        .awaitSingle()
}