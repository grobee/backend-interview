package org.deblock.integration.remote

import org.deblock.domain.flightsupplier.FlightSupplier
import org.deblock.domain.flightsupplier.FlightSupplierRequest
import org.deblock.domain.http.HttpClient
import org.deblock.domain.model.Flight

abstract class RemoteHttpFlightSuppler<R>(
    private val httpClient: HttpClient,
    private val responseType: Class<R>,
) : FlightSupplier {

    abstract val supplyEndpointPath: String

    abstract fun queryParams(request: FlightSupplierRequest): Map<String, String>

    abstract fun flights(response: R): List<Flight>

    override suspend fun supply(request: FlightSupplierRequest): List<Flight> {
        val response = sendGetRequest(request)
        return flights(response)
    }

    private suspend fun sendGetRequest(request: FlightSupplierRequest): R =
        httpClient.sendGet(supplyEndpointPath, responseType, queryParams(request))
}
