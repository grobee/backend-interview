package org.deblock.domain.model

import java.time.LocalDateTime
import org.deblock.domain.flightsupplier.FlightSupplierName

data class Flight(
    val airline: Airline,
    val supplier: FlightSupplierName,
    val fare: Fare,
    val departureAirportCode: AirportCode,
    val destinationAirportCode: AirportCode,
    val departureDate: LocalDateTime,
    val arrivalDate: LocalDateTime,
)