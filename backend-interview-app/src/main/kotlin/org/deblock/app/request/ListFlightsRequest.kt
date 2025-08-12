package org.deblock.org.deblock.app.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat

data class ListFlightsRequest(
    @field:NotBlank @field:Size(min = 3, max = 3) var origin: String?,
    @field:NotBlank @field:Size(min = 3, max = 3) var destination: String?,
    @field:NotNull @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE) var departureDate: LocalDate?,
    @field:NotNull @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE) var returnDate: LocalDate?,
    @field:NotNull @field:Min(1) @field:Max(4) var numberOfPassengers: Int?,
)
