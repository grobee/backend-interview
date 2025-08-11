package org.deblock.org.deblock.app

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import java.time.LocalDate
import net.javacrumbs.jsonunit.assertj.JsonAssertions.json
import net.javacrumbs.jsonunit.assertj.assertThatJson
import org.deblock.org.deblock.app.helpers.JsonTestData
import org.deblock.org.deblock.app.helpers.JsonTestData.EMPTY_JSON_BODY
import org.deblock.org.deblock.app.helpers.RequestParamsUtils.crazyAirRequestQueryParams
import org.deblock.org.deblock.app.helpers.RequestParamsUtils.toQueryString
import org.deblock.org.deblock.app.helpers.RequestParamsUtils.toughJetRequestQueryParams
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

    private val crazyAirMockServer = WireMockServer(8085).apply { start() }
    private val toughJetMockServer = WireMockServer(8086).apply { start() }

    @Test
    fun `should return empty response when all the suppliers have empty responses present`() {
        // given
        val origin = "origin"
        val destination = "destination"
        val departureDate = LocalDate.now()
        val returnDate = LocalDate.now()
        val numOfPassengers = 2

        crazyAirMockServer.stubFor(
            get(urlPathEqualTo("/list"))
                .withQueryParams(
                    crazyAirRequestQueryParams(origin, destination, departureDate, returnDate, numOfPassengers)
                )
                .willReturn(
                    ok()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withBody(EMPTY_JSON_BODY)
                )
        )
        toughJetMockServer.stubFor(
            get(urlPathEqualTo("/list"))
                .withQueryParams(
                    toughJetRequestQueryParams(origin, destination, departureDate, returnDate, numOfPassengers)
                )
                .willReturn(
                    ok()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withBody(EMPTY_JSON_BODY)
                )
        )

        // when
        val params = toQueryString(origin, destination, departureDate, returnDate, numOfPassengers)
        val response = restTemplate.getForEntity("/flights?$params", String::class.java)

        // then
        assertThatJson(checkNotNull(response.body))
            .isEqualTo(json(EMPTY_JSON_BODY))
    }


    @Test
    fun `should return all available flights when all the suppliers have responses present`() {
        // given
        val origin = "origin"
        val destination = "destination"
        val departureDate = LocalDate.now()
        val returnDate = LocalDate.now()
        val numOfPassengers = 2

        crazyAirMockServer.stubFor(
            get(urlPathEqualTo("/list"))
                .withQueryParams(
                    crazyAirRequestQueryParams(origin, destination, departureDate, returnDate, numOfPassengers)
                )
                .willReturn(
                    ok()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withBody(JsonTestData.Full.crazyAirResponse)
                )
        )
        toughJetMockServer.stubFor(
            get(urlPathEqualTo("/list"))
                .withQueryParams(
                    toughJetRequestQueryParams(origin, destination, departureDate, returnDate, numOfPassengers)
                )
                .willReturn(
                    ok()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withBody(JsonTestData.Full.toughJetResponse)
                )
        )

        // when
        val params = toQueryString(origin, destination, departureDate, returnDate, numOfPassengers)
        val response = restTemplate.getForEntity("/flights?$params", String::class.java)

        // then
        assertThatJson(checkNotNull(response.body))
            .isEqualTo(json(JsonTestData.Full.expectedResponse))
    }

    @Test
    fun `should return available flights when only some of the suppliers have responses present`() {
        // given
        val origin = "origin"
        val destination = "destination"
        val departureDate = LocalDate.now()
        val returnDate = LocalDate.now()
        val numOfPassengers = 2

        crazyAirMockServer.stubFor(
            get(urlPathEqualTo("/list"))
                .withQueryParams(
                    crazyAirRequestQueryParams(origin, destination, departureDate, returnDate, numOfPassengers)
                )
                .willReturn(
                    ok()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withBody(JsonTestData.Full.crazyAirResponse)
                )
        )
        toughJetMockServer.stubFor(
            get(urlPathEqualTo("/list"))
                .withQueryParams(
                    toughJetRequestQueryParams(origin, destination, departureDate, returnDate, numOfPassengers)
                )
                .willReturn(
                    ok()
                        .withHeader("Content-Type", APPLICATION_JSON.toString())
                        .withBody(EMPTY_JSON_BODY)
                )
        )

        // when
        val params = toQueryString(origin, destination, departureDate, returnDate, numOfPassengers)
        val response = restTemplate.getForEntity("/flights?$params", String::class.java)

        // then
        assertThatJson(checkNotNull(response.body))
            .isEqualTo(json(JsonTestData.Full.expectedResponse))
    }
}