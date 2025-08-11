package org.deblock.integration.remote.crazyair

import org.deblock.domain.flightsupplier.FlightSupplierName
import org.deblock.domain.model.Airline
import org.deblock.domain.model.AirportCode
import org.deblock.domain.model.Fare
import org.deblock.domain.model.Flight

object CrazyAirListResponseMapper {

    private const val SUPPLIER_NAME = "CRAZY_AIR"

    fun CrazyAirListResponse.Result.flight() = Flight(
        airline = Airline(airline),
        supplier = FlightSupplierName(SUPPLIER_NAME),
        fare = Fare(price),
        departureAirportCode = AirportCode(departureAirportCode),
        destinationAirportCode = AirportCode(destinationAirportCode),
        departureDate = departureDate,
        arrivalDate = arrivalDate,
    )
}