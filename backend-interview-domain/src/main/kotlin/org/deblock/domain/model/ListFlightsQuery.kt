package org.deblock.domain.model

import java.time.LocalDate

data class ListFlightsQuery(
    val origin: String,
    val destination: String,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    val numberOfPassengers: Int,
)