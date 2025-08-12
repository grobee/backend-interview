package org.deblock.org.deblock.app.helpers

import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import com.github.tomakehurst.wiremock.matching.StringValuePattern
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import org.springframework.http.MediaType.APPLICATION_JSON

object WireMockHelpers {

    fun WireMockExtension.stubListEndpoint(
        forQueryParams: Map<String, StringValuePattern>,
        responseBody: String
    ): StubMapping = stubFor(
        get(urlPathEqualTo("/list"))
            .withQueryParams(forQueryParams)
            .willReturn(
                ok()
                    .withHeader("Content-Type", APPLICATION_JSON.toString())
                    .withBody(responseBody)
            )
    )

    fun WireMockExtension.stubListEndpointWithErrorCode(
        forQueryParams: Map<String, StringValuePattern>,
        errorCode: Int,
    ): StubMapping = stubFor(
        get(urlPathEqualTo("/list"))
            .withQueryParams(forQueryParams)
            .willReturn(
                ok()
                    .withStatus(errorCode)
            )
    )
}