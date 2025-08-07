package org.deblock.domain.flightsupplier

import java.time.LocalDate

data class FlightSupplierRequest(
    val origin: String,
    val destination: String,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    val numberOfPassengers: Int,
)