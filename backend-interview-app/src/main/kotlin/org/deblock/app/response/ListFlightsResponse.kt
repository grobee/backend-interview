package org.deblock.org.deblock.app.response

import java.math.BigDecimal
import java.time.LocalDateTime

data class ListFlightsResponse(
    val airline: String,
    val supplier: String,
    val fare: BigDecimal,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    val departureDate: LocalDateTime,
    val arrivalDate: LocalDateTime,
)