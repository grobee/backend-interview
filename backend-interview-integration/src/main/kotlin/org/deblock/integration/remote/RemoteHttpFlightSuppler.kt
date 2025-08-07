package org.deblock.integration.remote

import kotlinx.coroutines.runBlocking
import org.deblock.domain.flightsupplier.FlightSupplier
import org.deblock.domain.flightsupplier.FlightSupplierRequest
import org.deblock.domain.model.Flight
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriBuilder

abstract class RemoteHttpFlightSuppler<R>(
    private val restClient: RestClient,
    private val responseType: Class<R>,
) : FlightSupplier {

    abstract val supplyEndpointPath: String

    abstract fun UriBuilder.queryParams(request: FlightSupplierRequest): UriBuilder

    abstract fun flights(response: R): List<Flight>

    override fun supply(request: FlightSupplierRequest): List<Flight> = runBlocking {
        val response = sendGetRequest(request)
        flights(response)
    }

    private fun sendGetRequest(request: FlightSupplierRequest): R =
        restClient.get()
            .uri { it.path(supplyEndpointPath).queryParams(request).build() }
            .retrieve()
            .body(responseType)
            ?: throw IllegalStateException("FlightSupplier response must have a body")
}
