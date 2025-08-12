package org.deblock.org.deblock.app.http

import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.core.Options.DYNAMIC_PORT
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import java.time.Duration.ofMillis
import kotlinx.coroutines.runBlocking
import net.javacrumbs.jsonunit.assertj.assertThatJson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry

class WebFluxHttpClientTest {

    companion object {
        @RegisterExtension
        private val wireMockServer = WireMockExtension.newInstance()
            .options(options().port(DYNAMIC_PORT))
            .build()
    }

    @Test
    fun test(): Unit = runBlocking {
        // given
        val webClient = WebClient.create("http://localhost:${wireMockServer.port}")
        val client = WebFluxHttpClient(webClient, Retry.fixedDelay(1, ofMillis(1)))

        wireMockServer.stubFor(
            get(urlPathEqualTo("/list"))
                .withQueryParams(
                    mapOf(
                        "param1" to equalTo("value1"),
                        "param2" to equalTo("value2")
                    )
                )
                // language=JSON
                .willReturn(
                    ok()
                        .withBody(
                            """
                                {
                                  "message": "something"    
                                }
                            """.trimIndent()
                        )
                )
        )

        // when
        val result = client.sendGet(
            endpoint = "/list",
            responseType = String::class.java,
            queryParams = mapOf(
                "param1" to "value1",
                "param2" to "value2"
            ),
        )

        // then
        wireMockServer.verify(
            getRequestedFor(urlPathEqualTo("/list"))
                .withQueryParam("param1", equalTo("value1"))
                .withQueryParam("param2", equalTo("value2"))
        )
        assertThatJson(result).isEqualTo(
            """
                {
                  "message": "something"    
                }
            """.trimIndent()
        )
    }
}