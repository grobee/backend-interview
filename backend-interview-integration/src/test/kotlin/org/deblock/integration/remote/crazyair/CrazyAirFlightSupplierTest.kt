package org.deblock.integration.remote.crazyair

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.deblock.domain.flightsupplier.FlightSupplierName
import org.deblock.domain.flightsupplier.FlightSupplierRequest
import org.deblock.domain.http.HttpClient
import org.deblock.domain.model.Airline
import org.deblock.domain.model.AirportCode
import org.deblock.domain.model.Fare
import org.deblock.domain.model.Flight
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CrazyAirFlightSupplierTest {

    @Test
    fun `should return correct flights when remote api provides results`(): Unit = runBlocking {
        // given
        val departureDate = LocalDateTime.now()
        val arrivalDate = LocalDateTime.now().plusDays(1)
        val mockHttpClient = mock<HttpClient> {
            whenever(
                it.sendGet(
                    endpoint = any(),
                    responseType = any<Class<CrazyAirListResponse>>(),
                    queryParams = any()
                )
            ).thenReturn(
                CrazyAirListResponse(
                    results = listOf(
                        CrazyAirListResponse.Result(
                            airline = "RYANAIR",
                            price = BigDecimal.valueOf(525.00),
                            cabinClass = "E",
                            departureAirportCode = "BUD",
                            destinationAirportCode = "BEG",
                            departureDate = departureDate,
                            arrivalDate = arrivalDate,
                        )
                    )
                )
            )
        }
        val supplier = CrazyAirFlightSupplier(mockHttpClient)

        // when
        val result = supplier.supply(
            FlightSupplierRequest(
                origin = AirportCode("BUD"),
                destination = AirportCode("BEG"),
                departureDate = departureDate.toLocalDate(),
                returnDate = arrivalDate.toLocalDate(),
                numberOfPassengers = 4,
            )
        )

        // then
        assertThat(result).isEqualTo(
            listOf(
                Flight(
                    airline = Airline("RYANAIR"),
                    supplier = FlightSupplierName("CRAZY_AIR"),
                    fare = Fare(BigDecimal.valueOf(525.00)),
                    departureAirportCode = AirportCode("BUD"),
                    destinationAirportCode = AirportCode("BEG"),
                    departureDate = departureDate,
                    arrivalDate = arrivalDate,
                )
            )
        )
    }

    @Test
    fun `should return no flights when remote api provides no results`(): Unit = runBlocking {
        // given
        val mockHttpClient = mock<HttpClient> {
            whenever(
                it.sendGet(
                    endpoint = any(),
                    responseType = any<Class<CrazyAirListResponse>>(),
                    queryParams = any()
                )
            ).thenReturn(
                CrazyAirListResponse(results = emptyList())
            )
        }
        val supplier = CrazyAirFlightSupplier(mockHttpClient)

        // when
        val result = supplier.supply(
            FlightSupplierRequest(
                origin = AirportCode("BUD"),
                destination = AirportCode("BEG"),
                departureDate = LocalDate.now(),
                returnDate = LocalDate.now().plusDays(1),
                numberOfPassengers = 4,
            )
        )

        // then
        assertThat(result).isEqualTo(emptyList<Flight>())
    }
}