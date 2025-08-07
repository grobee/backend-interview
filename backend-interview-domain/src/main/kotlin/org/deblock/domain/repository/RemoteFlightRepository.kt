package org.deblock.domain.repository

import org.deblock.domain.flightsupplier.FlightSupplier
import org.deblock.domain.flightsupplier.FlightSupplierRequest
import org.deblock.domain.model.Flight
import org.deblock.domain.model.ListFlightsQuery

class RemoteFlightRepository(
    val flightSuppliers: List<FlightSupplier>,
) : FlightRepository {

    override fun getAll(query: ListFlightsQuery): List<Flight> =
        flightSuppliers
            .flatMap { it.supply(flightSupplierRequest(query)) }
            .toList()

    private fun flightSupplierRequest(query: ListFlightsQuery) = FlightSupplierRequest(
        origin = query.origin,
        destination = query.destination,
        departureDate = query.departureDate,
        returnDate = query.returnDate,
        numberOfPassengers = query.numberOfPassengers,
    )
}
