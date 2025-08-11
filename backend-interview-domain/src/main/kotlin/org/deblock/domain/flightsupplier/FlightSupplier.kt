package org.deblock.domain.flightsupplier

import org.deblock.domain.model.Flight

interface FlightSupplier {

    suspend fun supply(request: FlightSupplierRequest): List<Flight>
}