package org.deblock.integration.remote.crazyair

import org.deblock.domain.flightsupplier.FlightSupplierName
import org.deblock.domain.flightsupplier.FlightSupplierRequest
import org.deblock.domain.http.HttpClient
import org.deblock.domain.model.Flight
import org.deblock.integration.remote.RemoteHttpFlightSuppler
import org.deblock.integration.remote.crazyair.CrazyAirListResponseMapper.flight

class CrazyAirFlightSupplier(
    httpClient: HttpClient,
    override val supplyEndpointPath: String = "/list",
) : RemoteHttpFlightSuppler<CrazyAirListResponse>(
    httpClient,
    CrazyAirListResponse::class.java,
) {

    override val supplierName = FlightSupplierName("CRAZY_AIR")

    override fun queryParams(request: FlightSupplierRequest) = mapOf(
        "origin" to request.origin.value,
        "destination" to request.destination.value,
        "departureDate" to request.departureDate.toString(),
        "returnDate" to request.returnDate.toString(),
        "passengerCount" to request.numberOfPassengers.toString(),
    )

    override fun flights(response: CrazyAirListResponse): List<Flight> = response
        .results
        .map { it.flight() }
}
