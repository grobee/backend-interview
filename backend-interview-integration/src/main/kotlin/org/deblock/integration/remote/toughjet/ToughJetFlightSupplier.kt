package org.deblock.integration.remote.toughjet

import org.deblock.domain.flightsupplier.FlightSupplierRequest
import org.deblock.domain.http.HttpClient
import org.deblock.domain.model.Flight
import org.deblock.integration.remote.RemoteHttpFlightSuppler
import org.deblock.integration.remote.toughjet.ToughJetFlightResponseMapper.flight

class ToughJetFlightSupplier(
    httpClient: HttpClient,
    override val supplyEndpointPath: String = "/list",
) : RemoteHttpFlightSuppler<ToughJetListResponse>(
    httpClient,
    ToughJetListResponse::class.java,
) {

    override fun queryParams(request: FlightSupplierRequest) = mapOf(
        "from" to request.origin,
        "to" to request.destination,
        "outboundDate" to request.departureDate.toString(),
        "inboundDate" to request.returnDate.toString(),
        "numberOfAdults" to request.numberOfPassengers.toString(),
    )

    override fun flights(response: ToughJetListResponse): List<Flight> = response
        .results
        .map {
            it.flight(
                ToughJetDiscountedPriceCalculator.calculate(
                    basePrice = it.basePrice,
                    discount = it.discount,
                    tax = it.tax,
                )
            )
        }
}