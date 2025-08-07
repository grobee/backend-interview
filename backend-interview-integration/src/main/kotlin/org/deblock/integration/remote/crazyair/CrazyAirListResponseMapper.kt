package org.deblock.integration.remote.crazyair

import org.deblock.domain.model.Flight

object CrazyAirListResponseMapper {

    private const val SUPPLIER_NAME = "CrazyAir"

    fun CrazyAirListResponse.Result.flight() = Flight(
        airline = airline,
        supplier = SUPPLIER_NAME,
        fare = price,
        departureAirportCode = departureAirportCode,
        destinationAirportCode = destinationAirportCode,
        departureDate = departureDate,
        arrivalDate = arrivalDate,
    )
}