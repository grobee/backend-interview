package org.deblock.integration.remote.toughjet

import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import org.deblock.domain.flightsupplier.FlightSupplierName
import org.deblock.domain.model.Airline
import org.deblock.domain.model.AirportCode
import org.deblock.domain.model.Fare
import org.deblock.domain.model.Flight

object ToughJetFlightResponseMapper {

    private const val SUPPLIER_NAME = "TOUGH_JET"

    fun ToughJetListResponse.Result.flight(discountedPrice: BigDecimal) = Flight(
        airline = Airline(carrier),
        supplier = FlightSupplierName(SUPPLIER_NAME),
        fare = Fare(discountedPrice),
        departureAirportCode = AirportCode(departureAirportName),
        destinationAirportCode = AirportCode(arrivalAirportName),
        departureDate = LocalDateTime.ofInstant(outboundDateTime, UTC),
        arrivalDate = LocalDateTime.ofInstant(inboundDateTime, UTC),
    )
}