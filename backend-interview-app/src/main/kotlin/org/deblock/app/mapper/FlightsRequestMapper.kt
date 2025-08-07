package org.deblock.org.deblock.app.mapper

import org.deblock.domain.model.ListFlightsQuery
import org.deblock.org.deblock.app.request.ListFlightsRequest

object FlightsRequestMapper {

    fun listFlightsQuery(request: ListFlightsRequest) = ListFlightsQuery(
        origin = request.origin,
        destination = request.destination,
        departureDate = request.departureDate,
        returnDate = request.returnDate,
        numberOfPassengers = request.numberOfPassengers,
    )
}