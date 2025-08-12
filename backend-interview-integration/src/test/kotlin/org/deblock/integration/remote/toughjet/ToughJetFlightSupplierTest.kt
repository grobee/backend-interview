package org.deblock.integration.remote.toughjet

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.temporal.ChronoUnit.DAYS
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

class ToughJetFlightSupplierTest {

    @Test
    fun `should return correct flights when remote api provides results`(): Unit = runBlocking {
        // given
        val outboundDateTime = Instant.now().atZone(UTC)
        val inboundDateTime = Instant.now().plus(1, DAYS).atZone(UTC)
        val mockHttpClient = mock<HttpClient> {
            whenever(
                it.sendGet(
                    endpoint = any(),
                    responseType = any<Class<ToughJetListResponse>>(),
                    queryParams = any()
                )
            ).thenReturn(
                ToughJetListResponse(
                    results = listOf(
                        ToughJetListResponse.Result(
                            carrier = "RYANAIR",
                            basePrice = BigDecimal.valueOf(255),
                            tax = BigDecimal.valueOf(20),
                            discount = 5,
                            departureAirportName = "BUD",
                            arrivalAirportName = "BEG",
                            outboundDateTime = outboundDateTime.toInstant(),
                            inboundDateTime = inboundDateTime.toInstant(),
                        )
                    )
                )
            )
        }
        val supplier = ToughJetFlightSupplier(mockHttpClient)

        // when
        val result = supplier.supply(
            FlightSupplierRequest(
                origin = AirportCode("BUD"),
                destination = AirportCode("BEG"),
                departureDate = LocalDate.from(outboundDateTime),
                returnDate = LocalDate.from(inboundDateTime),
                numberOfPassengers = 4,
            )
        )

        // then
        assertThat(result).isEqualTo(
            listOf(
                Flight(
                    airline = Airline("RYANAIR"),
                    supplier = FlightSupplierName("TOUGH_JET"),
                    fare = Fare(BigDecimal.valueOf(262.25)),
                    departureAirportCode = AirportCode("BUD"),
                    destinationAirportCode = AirportCode("BEG"),
                    departureDate = LocalDateTime.from(outboundDateTime),
                    arrivalDate = LocalDateTime.from(inboundDateTime),
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
                    responseType = any<Class<ToughJetListResponse>>(),
                    queryParams = any()
                )
            ).thenReturn(
                ToughJetListResponse(results = emptyList())
            )
        }
        val supplier = ToughJetFlightSupplier(mockHttpClient)

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