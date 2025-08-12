package org.deblock.domain.flightsupplier

import org.deblock.domain.model.Flight

interface FlightSupplier {

    val supplierName: FlightSupplierName

    suspend fun supply(request: FlightSupplierRequest): List<Flight>
}