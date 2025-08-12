package org.deblock.domain.model

import java.time.LocalDate

data class ListFlightsQuery(
    val origin: AirportCode,
    val destination: AirportCode,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    val numberOfPassengers: Int,
)