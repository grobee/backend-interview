package org.deblock.integration.remote.toughjet.response

import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal
import java.time.LocalDateTime

data class ToughJetListResponse(
    @JsonValue
    val results: List<Result>,
) {
    data class Result(
        val airline: String,
        val supplier: String,
        val fare: BigDecimal,
        val departureAirportCode: String,
        val destinationAirportCode: String,
        val departureDate: LocalDateTime,
        val arrivalDate: LocalDateTime,
    )
}