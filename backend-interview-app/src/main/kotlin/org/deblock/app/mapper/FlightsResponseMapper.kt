package org.deblock.org.deblock.app.mapper

import org.deblock.domain.model.Flight
import org.deblock.org.deblock.app.response.ListFlightsResponse

object FlightsResponseMapper {

    fun listFlightsResponse(flights: List<Flight>) = flights.map {
        ListFlightsResponse(
            airline = it.airline,
            supplier = it.supplier,
            fare = it.fare,
            departureAirportCode = it.departureAirportCode,
            destinationAirportCode = it.destinationAirportCode,
            departureDate = it.departureDate,
            arrivalDate = it.arrivalDate,
        )
    }
}