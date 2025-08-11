package org.deblock.domain.repository

import org.deblock.domain.model.Flight
import org.deblock.domain.model.ListFlightsQuery

interface FlightRepository {

    suspend fun getAll(query: ListFlightsQuery): List<Flight>
}