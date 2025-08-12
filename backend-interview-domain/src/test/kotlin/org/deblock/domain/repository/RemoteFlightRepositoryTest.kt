package org.deblock.domain.repository

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.DAYS
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.deblock.domain.flightsupplier.FlightSupplier
import org.deblock.domain.flightsupplier.FlightSupplierName
import org.deblock.domain.model.Airline
import org.deblock.domain.model.AirportCode
import org.deblock.domain.model.Fare
import org.deblock.domain.model.Flight
import org.deblock.domain.model.ListFlightsQuery
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RemoteFlightRepositoryTest {

    @Test
    fun `should return empty when no suppliers are present`(): Unit = runBlocking {
        // given
        val suppliers: List<FlightSupplier> = listOf()
        val repository = RemoteFlightRepository(suppliers)

        // when
        val query = ListFlightsQuery(
            origin = AirportCode("BUD"),
            destination = AirportCode("BEG"),
            departureDate = LocalDate.now(),
            returnDate = LocalDate.now().plus(1, DAYS),
            numberOfPassengers = 2,
        )
        val result = repository.getAll(query)

        // then
        assertThat(result).isEqualTo(emptyList<Flight>())
    }

    @Test
    fun `should return result in correct order when multiple suppliers are present`(): Unit = runBlocking {
        // given
        val flight1 = Flight(
            airline = Airline("RYANAIR"),
            supplier = FlightSupplierName("CRAZY_AIR"),
            fare = Fare(BigDecimal.valueOf(255.55)),
            departureAirportCode = AirportCode("BUD"),
            destinationAirportCode = AirportCode("BEG"),
            departureDate = LocalDateTime.now(),
            arrivalDate = LocalDateTime.now().plusHours(3),
        )
        val flight2 = Flight(
            airline = Airline("WIZZAIR"),
            supplier = FlightSupplierName("TOUGH_JET"),
            fare = Fare(BigDecimal.valueOf(200.10)),
            departureAirportCode = AirportCode("BUD"),
            destinationAirportCode = AirportCode("MAD"),
            departureDate = LocalDateTime.now(),
            arrivalDate = LocalDateTime.now().plusHours(4),
        )

        val supplier1 = mock<FlightSupplier> {
            whenever(it.supply(any())).thenReturn(listOf(flight1))
        }
        val supplier2 = mock<FlightSupplier> {
            whenever(it.supply(any())).thenReturn(listOf(flight2))
        }
        val suppliers: List<FlightSupplier> = listOf(supplier1, supplier2)

        val repository = RemoteFlightRepository(suppliers)

        // when
        val query = ListFlightsQuery(
            origin = AirportCode("BUD"),
            destination = AirportCode("BEG"),
            departureDate = LocalDate.now(),
            returnDate = LocalDate.now().plus(1, DAYS),
            numberOfPassengers = 2,
        )
        val result = repository.getAll(query)

        // then
        assertThat(result).isEqualTo(listOf(flight2, flight1))
    }

    @Test
    fun `should return result when some suppliers fail with exception`(): Unit = runBlocking {
        // given
        val flight1 = Flight(
            airline = Airline("RYANAIR"),
            supplier = FlightSupplierName("CRAZY_AIR"),
            fare = Fare(BigDecimal.valueOf(255.55)),
            departureAirportCode = AirportCode("BUD"),
            destinationAirportCode = AirportCode("BEG"),
            departureDate = LocalDateTime.now(),
            arrivalDate = LocalDateTime.now().plusHours(3),
        )

        val supplier1 = mock<FlightSupplier> {
            whenever(it.supply(any())).thenReturn(listOf(flight1))
        }
        val supplier2 = mock<FlightSupplier> {
            whenever(it.supply(any()))
                .thenThrow(IllegalArgumentException("Something bad happened"))
        }
        val suppliers: List<FlightSupplier> = listOf(supplier1, supplier2)

        val repository = RemoteFlightRepository(suppliers)

        // when
        val query = ListFlightsQuery(
            origin = AirportCode("BUD"),
            destination = AirportCode("BEG"),
            departureDate = LocalDate.now(),
            returnDate = LocalDate.now().plus(1, DAYS),
            numberOfPassengers = 2,
        )
        val result = repository.getAll(query)

        // then
        assertThat(result).isEqualTo(listOf(flight1))
    }
}