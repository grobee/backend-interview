package org.deblock.org.deblock.app

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.MediaType.APPLICATION_JSON

@SpringBootTest(webEnvironment = RANDOM_PORT)
class FlightsControllerTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private val crazyAirMockServer: WireMockServer = WireMockServer(8085)
        .apply { start() }
    private val toughJetMockServer: WireMockServer = WireMockServer(8086)
        .apply { start() }

    @Test
    fun test() {
        // given
        val origin = "origin"
        val destination = "destination"
        val departureDate = LocalDate.now()
        val returnDate = LocalDate.now()
        val numOfPassengers = 2

        crazyAirMockServer.stubFor(
            get(urlPathEqualTo("/list"))
                .withQueryParams(
                    mapOf(
                        "origin" to equalTo(origin),
                        "destination" to equalTo(destination),
                        "departureDate" to equalTo(departureDate.toString()),
                        "returnDate" to equalTo(returnDate.toString()),
                        "passengerCount" to equalTo("$numOfPassengers")
                    )
                )
                .willReturn(
                    ok()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        //language=JSON
                        .withBody(
                            """[
                                          {
                                            "airline": "WizzAir",
                                            "price": 500,
                                            "cabinClass": "EE",
                                            "departureAirportCode": "AET",
                                            "destinationAirportCode": "BUD",
                                            "departureDate": "${LocalDateTime.of(departureDate, LocalTime.now())}",
                                            "arrivalDate": "${LocalDateTime.of(returnDate, LocalTime.now())}"
                                          }
                                    ]""".trimIndent()
                        )
                )
        )
        toughJetMockServer.stubFor(
            get(urlPathEqualTo("/list"))
                .withQueryParams(
                    mapOf(
                        "from" to equalTo(origin),
                        "to" to equalTo(destination),
                        "outboundDate" to equalTo(departureDate.toString()),
                        "inboundDate" to equalTo(returnDate.toString()),
                        "numberOfAdults" to equalTo("$numOfPassengers")
                    )
                )
                .willReturn(
                    ok()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        //language=JSON
                        .withBody(
                            """[
                                          {
                                            "carrier": "WizzAir",
                                            "basePrice": 700,
                                            "tax": 20,
                                            "discount": 5,
                                            "departureAirportName": "BUK",
                                            "arrivalAirportName": "BUD",
                                            "outboundDateTime": "${Instant.now()}",
                                            "inboundDateTime": "${Instant.now()}"
                                          }
                                    ]""".trimIndent()
                        )
                )
        )

        val params = "origin=$origin&destination=$destination&departureDate=$departureDate" +
            "&returnDate=$returnDate&numberOfPassengers=$numOfPassengers"

        // when
        val response = restTemplate.getForEntity("/flights?$params", String::class.java)

        // then
        println(response)
    }
}