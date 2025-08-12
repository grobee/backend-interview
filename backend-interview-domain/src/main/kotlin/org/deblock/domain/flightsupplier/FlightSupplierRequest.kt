package org.deblock.domain.flightsupplier

import java.time.LocalDate
import org.deblock.domain.model.AirportCode

data class FlightSupplierRequest(
    val origin: AirportCode,
    val destination: AirportCode,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    val numberOfPassengers: Int,
)