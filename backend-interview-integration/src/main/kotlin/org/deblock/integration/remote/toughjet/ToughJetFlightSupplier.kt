package org.deblock.integration.remote.toughjet

import org.deblock.domain.flightsupplier.FlightSupplierRequest
import org.deblock.domain.model.Flight
import org.deblock.integration.remote.RemoteHttpFlightSuppler
import org.deblock.integration.remote.toughjet.ToughJetFlightResponseMapper.flight
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriBuilder

class ToughJetFlightSupplier(
    restClient: RestClient,
    override val supplyEndpointPath: String = "/list",
) : RemoteHttpFlightSuppler<ToughJetListResponse>(
    restClient,
    ToughJetListResponse::class.java,
) {

    override fun UriBuilder.queryParams(request: FlightSupplierRequest) = apply {
        queryParam("from", request.origin)
        queryParam("to", request.destination)
        queryParam("outboundDate", request.departureDate.toString())
        queryParam("inboundDate", request.returnDate.toString())
        queryParam("numberOfAdults", request.numberOfPassengers.toString())
    }

    override fun flights(response: ToughJetListResponse): List<Flight> = response
        .results
        .map {
            it.flight(ToughJetDiscountedPriceCalculator.calculate(it))
        }
}