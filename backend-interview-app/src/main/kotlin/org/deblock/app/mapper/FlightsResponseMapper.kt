package org.deblock.org.deblock.app.mapper

import org.deblock.domain.model.Flight
import org.deblock.org.deblock.app.response.ListFlightsResponse

object FlightsResponseMapper {

    fun listFlightsResponse(flights: List<Flight>) = flights.map {
        ListFlightsResponse(
            airline = it.airline.value,
            supplier = it.supplier.value,
            fare = it.fare.value,
            departureAirportCode = it.departureAirportCode.value,
            destinationAirportCode = it.destinationAirportCode.value,
            departureDate = it.departureDate,
            arrivalDate = it.arrivalDate,
        )
    }
}