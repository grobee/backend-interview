package org.deblock.org.deblock.app

import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import java.time.LocalDate
import net.javacrumbs.jsonunit.assertj.JsonAssertions.json
import net.javacrumbs.jsonunit.assertj.assertThatJson
import net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER
import org.assertj.core.api.Assertions.assertThat
import org.deblock.org.deblock.app.helpers.JsonTestData
import org.deblock.org.deblock.app.helpers.JsonTestData.EMPTY_JSON_BODY
import org.deblock.org.deblock.app.helpers.RequestParamsUtils.crazyAirRequestQueryParams
import org.deblock.org.deblock.app.helpers.RequestParamsUtils.toQueryString
import org.deblock.org.deblock.app.helpers.RequestParamsUtils.toughJetRequestQueryParams
import org.deblock.org.deblock.app.helpers.WireMockHelpers.stubListEndpoint
import org.deblock.org.deblock.app.helpers.WireMockHelpers.stubListEndpointWithErrorCode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

@SpringBootTest(webEnvironment = RANDOM_PORT)
class FlightsControllerFunctionalTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    companion object {
        @RegisterExtension
        val crazyAirMockServer: WireMockExtension = WireMockExtension.newInstance()
            .options(options().port(8085))
            .build()

        @RegisterExtension
        val toughJetMockServer: WireMockExtension = WireMockExtension.newInstance()
            .options(options().port(8086))
            .build()
    }

    @Test
    fun `should return empty response when all the suppliers have empty responses present`() {
        // given
        val origin = "BUD"
        val destination = "BEG"
        val departureDate = LocalDate.of(2025, 9, 15)
        val returnDate = LocalDate.of(2025, 9, 16)
        val numOfPassengers = 2

        crazyAirMockServer.stubListEndpoint(
            forQueryParams = crazyAirRequestQueryParams(
                origin,
                destination,
                departureDate,
                returnDate,
                numOfPassengers
            ),
            responseBody = EMPTY_JSON_BODY,
        )
        toughJetMockServer.stubListEndpoint(
            forQueryParams = toughJetRequestQueryParams(
                origin,
                destination,
                departureDate,
                returnDate,
                numOfPassengers
            ),
            responseBody = EMPTY_JSON_BODY,
        )

        // when
        val params = toQueryString(origin, destination, departureDate, returnDate, numOfPassengers)
        val response = restTemplate.getForEntity("/flights?$params", String::class.java)

        // then
        assertThatJson(requireNotNull(response.body))
            .isEqualTo(json(EMPTY_JSON_BODY))
    }


    @Test
    fun `should return all available flights when all the suppliers have responses present`() {
        // given
        val origin = "BUD"
        val destination = "BEG"
        val departureDate = LocalDate.of(2025, 9, 15)
        val returnDate = LocalDate.of(2025, 9, 16)
        val numOfPassengers = 2

        crazyAirMockServer.stubListEndpoint(
            forQueryParams = crazyAirRequestQueryParams(
                origin,
                destination,
                departureDate,
                returnDate,
                numOfPassengers
            ),
            responseBody = JsonTestData.Full.crazyAirResponse,
        )
        toughJetMockServer.stubListEndpoint(
            forQueryParams = toughJetRequestQueryParams(
                origin,
                destination,
                departureDate,
                returnDate,
                numOfPassengers
            ),
            responseBody = JsonTestData.Full.toughJetResponse,
        )

        // when
        val params = toQueryString(origin, destination, departureDate, returnDate, numOfPassengers)
        val response = restTemplate.getForEntity("/flights?$params", String::class.java)

        // then
        assertThatJson(requireNotNull(response.body))
            .isEqualTo(json(JsonTestData.Full.expectedResponse))
    }

    @Test
    fun `should return available flights when only some of the suppliers have responses present`() {
        // given
        val origin = "BUD"
        val destination = "BEG"
        val departureDate = LocalDate.of(2025, 9, 15)
        val returnDate = LocalDate.of(2025, 9, 16)
        val numOfPassengers = 2

        crazyAirMockServer.stubListEndpoint(
            forQueryParams = crazyAirRequestQueryParams(
                origin,
                destination,
                departureDate,
                returnDate,
                numOfPassengers
            ),
            responseBody = JsonTestData.Partial.crazyAirResponse,
        )
        toughJetMockServer.stubListEndpoint(
            forQueryParams = toughJetRequestQueryParams(
                origin,
                destination,
                departureDate,
                returnDate,
                numOfPassengers
            ),
            responseBody = EMPTY_JSON_BODY,
        )

        // when
        val params = toQueryString(origin, destination, departureDate, returnDate, numOfPassengers)
        val response = restTemplate.getForEntity("/flights?$params", String::class.java)

        // then
        assertThatJson(requireNotNull(response.body))
            .isEqualTo(json(JsonTestData.Partial.expectedResponse))
    }

    @Test
    fun `should return available flights when some of the suppliers return with error code`() {
        // given
        val origin = "BUD"
        val destination = "BEG"
        val departureDate = LocalDate.of(2025, 9, 15)
        val returnDate = LocalDate.of(2025, 9, 16)
        val numOfPassengers = 2

        crazyAirMockServer.stubListEndpoint(
            forQueryParams = crazyAirRequestQueryParams(
                origin,
                destination,
                departureDate,
                returnDate,
                numOfPassengers
            ),
            responseBody = JsonTestData.Partial.crazyAirResponse,
        )
        toughJetMockServer.stubListEndpointWithErrorCode(
            forQueryParams = toughJetRequestQueryParams(
                origin,
                destination,
                departureDate,
                returnDate,
                numOfPassengers
            ),
            errorCode = INTERNAL_SERVER_ERROR.value(),
        )

        // when
        val params = toQueryString(origin, destination, departureDate, returnDate, numOfPassengers)
        val response = restTemplate.getForEntity("/flights?$params", String::class.java)

        // then
        assertThatJson(requireNotNull(response.body))
            .isEqualTo(json(JsonTestData.Partial.expectedResponse))
    }

    @Test
    fun `should fail request validation with invalid data`() {
        // given
        val origin = "ORIGIN"
        val destination = "A"
        val departureDate = LocalDate.of(2025, 9, 15)
        val returnDate = LocalDate.of(2025, 9, 16)
        val numOfPassengers = 5

        // when
        val params = toQueryString(origin, destination, departureDate, returnDate, numOfPassengers)
        val response = restTemplate.getForEntity("/flights?$params", String::class.java)

        // then
        assertThat(response.statusCode.is4xxClientError)
        // language=JSON
        assertThatJson(requireNotNull(response.body))
            .`when`(IGNORING_ARRAY_ORDER)
            .isEqualTo(
                """
                {
                  "message" : "Error occurred while validating request parameters",
                  "errorCode" : 400,
                  "errors" : [ 
                    "Field numberOfPassengers must be less than or equal to 4, rejected value: 5", 
                    "Field origin size must be between 3 and 3, rejected value: ORIGIN", 
                    "Field destination size must be between 3 and 3, rejected value: A" 
                  ]
                }
                """.trimIndent()
            )
    }

    @Test
    fun `should fail request validation with non-present params`() {
        // given
        val emptyQueryParams = ""

        // when
        val response = restTemplate.getForEntity("/flights?$emptyQueryParams", String::class.java)

        // then
        assertThat(response.statusCode.is4xxClientError)
        // language=JSON
        assertThatJson(requireNotNull(response.body))
            .`when`(IGNORING_ARRAY_ORDER)
            .isEqualTo(
                """
                {
                  "message" : "Error occurred while validating request parameters",
                  "errorCode" : 400,
                  "errors" : [ 
                      "Field destination must not be blank, rejected value: null", 
                      "Field departureDate must not be null, rejected value: null", 
                      "Field numberOfPassengers must not be null, rejected value: null", 
                      "Field origin must not be blank, rejected value: null", 
                      "Field returnDate must not be null, rejected value: null"
                  ]
                }
                 """.trimIndent()
            )
    }
}