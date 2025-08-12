package org.deblock.org.deblock.app.mapper

import org.deblock.domain.model.AirportCode
import org.deblock.domain.model.ListFlightsQuery
import org.deblock.org.deblock.app.request.ListFlightsRequest

object FlightsRequestMapper {

    fun listFlightsQuery(request: ListFlightsRequest) = ListFlightsQuery(
        origin = requireNotNull(request.origin) { "origin can't be null" }
            .let(::AirportCode),
        destination = requireNotNull(request.destination) { "destination can't be null" }
            .let(::AirportCode),
        departureDate = requireNotNull(request.departureDate) { "departureDate can't be null" },
        returnDate = requireNotNull(request.returnDate) { "returnDate can't be null" },
        numberOfPassengers = requireNotNull(request.numberOfPassengers) { "numberOfPassengers can't be null" },
    )
}