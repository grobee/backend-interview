package org.deblock.integration.remote.crazyair

import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal
import java.time.LocalDateTime

data class CrazyAirListResponse(
    @JsonValue
    val results: List<Result>,
) {
    data class Result(
        val airline: String,
        val price: BigDecimal,
        val cabinClass: String,
        val departureAirportCode: String,
        val destinationAirportCode: String,
        val departureDate: LocalDateTime,
        val arrivalDate: LocalDateTime,
    )
}