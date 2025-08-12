package org.deblock.domain.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import mu.KotlinLogging
import org.deblock.domain.flightsupplier.FlightSupplier
import org.deblock.domain.flightsupplier.FlightSupplierRequest
import org.deblock.domain.model.Flight
import org.deblock.domain.model.ListFlightsQuery

class RemoteFlightRepository(
    private val flightSuppliers: List<FlightSupplier>,
) : FlightRepository {

    private val logger = KotlinLogging.logger { }

    override suspend fun getAll(query: ListFlightsQuery): List<Flight> = coroutineScope {
        flightSuppliers
            .map { async { it.supplyFlights(query) } }
            .awaitAll()
            .flatten()
            .sortedBy { it.fare }
            .toList()
    }

    private suspend fun FlightSupplier.supplyFlights(query: ListFlightsQuery): List<Flight> =
        runCatching { supply(flightSupplierRequest(query)) }
            .onFailure { logger.error(it) { "Couldn't fetch flights from supplier $supplierName" } }
            .getOrDefault(emptyList())

    private fun flightSupplierRequest(query: ListFlightsQuery) = FlightSupplierRequest(
        origin = query.origin,
        destination = query.destination,
        departureDate = query.departureDate,
        returnDate = query.returnDate,
        numberOfPassengers = query.numberOfPassengers,
    )
}
