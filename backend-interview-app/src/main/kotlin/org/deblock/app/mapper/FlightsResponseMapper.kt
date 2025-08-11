package org.deblock.org.deblock.app.mapper

import java.math.RoundingMode
import org.deblock.domain.model.Flight
import org.deblock.org.deblock.app.response.ListFlightsResponse

object FlightsResponseMapper {

    private const val FARE_ROUNDING_PRECISION = 2

    fun listFlightsResponse(flights: List<Flight>) = flights.map {
        ListFlightsResponse(
            airline = it.airline.value,
            supplier = it.supplier.value,
            fare = it.fare.value.setScale(FARE_ROUNDING_PRECISION, RoundingMode.UP),
            departureAirportCode = it.departureAirportCode.value,
            destinationAirportCode = it.destinationAirportCode.value,
            departureDate = it.departureDate,
            arrivalDate = it.arrivalDate,
        )
    }
}