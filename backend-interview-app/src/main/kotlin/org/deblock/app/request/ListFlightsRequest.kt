package org.deblock.org.deblock.app.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class ListFlightsRequest(
    @param:NotBlank val origin: String,
    @param:NotBlank val destination: String,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    @param:Min(1) @param:Max(4) val numberOfPassengers: Int,
)
