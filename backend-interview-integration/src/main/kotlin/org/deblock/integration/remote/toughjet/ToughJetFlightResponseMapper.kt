package org.deblock.integration.remote.toughjet

import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import org.deblock.domain.model.Flight

object ToughJetFlightResponseMapper {

    private const val SUPPLIER_NAME = "ToughJet"

    fun ToughJetListResponse.Result.flight() = Flight(
        airline = carrier,
        supplier = SUPPLIER_NAME,
        fare = basePrice,
        departureAirportCode = departureAirportName,
        destinationAirportCode = arrivalAirportName,
        departureDate = LocalDateTime.ofInstant(outboundDateTime, UTC),
        arrivalDate = LocalDateTime.ofInstant(inboundDateTime, UTC),
    )
}