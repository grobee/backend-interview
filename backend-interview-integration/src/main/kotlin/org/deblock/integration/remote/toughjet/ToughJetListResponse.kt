package org.deblock.integration.remote.toughjet

import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal
import java.time.Instant

data class ToughJetListResponse(
    @JsonValue
    val results: List<Result>,
) {
    data class Result(
        val carrier: String,
        val basePrice: BigDecimal,
        val tax: BigDecimal,
        val discount: Int,
        val departureAirportName: String,
        val arrivalAirportName: String,
        val outboundDateTime: Instant,
        val inboundDateTime: Instant,
    )
}