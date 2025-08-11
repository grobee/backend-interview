package org.deblock.domain.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import org.deblock.domain.flightsupplier.FlightSupplier
import org.deblock.domain.flightsupplier.FlightSupplierRequest
import org.deblock.domain.model.Flight
import org.deblock.domain.model.ListFlightsQuery

class RemoteFlightRepository(
    val flightSuppliers: List<FlightSupplier>,
) : FlightRepository {

    override suspend fun getAll(query: ListFlightsQuery): List<Flight> = supervisorScope {
        flightSuppliers
            .map {
                async {
                    it.supply(flightSupplierRequest(query))
                }
            }
            .awaitAll()
            .flatten()
            .sortedBy { it.fare }
            .toList()
    }

    private fun flightSupplierRequest(query: ListFlightsQuery) = FlightSupplierRequest(
        origin = query.origin,
        destination = query.destination,
        departureDate = query.departureDate,
        returnDate = query.returnDate,
        numberOfPassengers = query.numberOfPassengers,
    )
}
