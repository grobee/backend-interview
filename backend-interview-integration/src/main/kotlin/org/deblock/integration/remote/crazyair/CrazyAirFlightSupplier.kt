package org.deblock.integration.remote.crazyair

import org.deblock.domain.flightsupplier.FlightSupplierRequest
import org.deblock.domain.model.Flight
import org.deblock.integration.remote.RemoteHttpFlightSuppler
import org.deblock.integration.remote.crazyair.CrazyAirListResponseMapper.flight
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriBuilder

class CrazyAirFlightSupplier(
    restClient: RestClient,
    override val supplyEndpointPath: String = "/list",
) : RemoteHttpFlightSuppler<CrazyAirListResponse>(
    restClient,
    CrazyAirListResponse::class.java,
) {

    override fun UriBuilder.queryParams(request: FlightSupplierRequest) = apply {
        queryParam("origin", request.origin)
        queryParam("destination", request.destination)
        queryParam("departureDate", request.departureDate.toString())
        queryParam("returnDate", request.returnDate.toString())
        queryParam("passengerCount", request.numberOfPassengers.toString())
    }

    override fun flights(response: CrazyAirListResponse): List<Flight> = response
        .results
        .map { it.flight() }
}
