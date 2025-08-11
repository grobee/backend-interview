package org.deblock.org.deblock.app.helpers

import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import java.time.LocalDate

object RequestParamsUtils {

    fun crazyAirRequestQueryParams(
        origin: String,
        destination: String,
        departureDate: LocalDate,
        returnDate: LocalDate,
        numOfPassengers: Int
    ) = mapOf(
        "origin" to equalTo(origin),
        "destination" to equalTo(destination),
        "departureDate" to equalTo(departureDate.toString()),
        "returnDate" to equalTo(returnDate.toString()),
        "passengerCount" to equalTo(numOfPassengers.toString())
    )

    fun toughJetRequestQueryParams(
        origin: String,
        destination: String,
        departureDate: LocalDate,
        returnDate: LocalDate,
        numOfPassengers: Int
    ) = mapOf(
        "from" to equalTo(origin),
        "to" to equalTo(destination),
        "outboundDate" to equalTo(departureDate.toString()),
        "inboundDate" to equalTo(returnDate.toString()),
        "numberOfAdults" to equalTo("$numOfPassengers")
    )

    fun toQueryString(
        origin: String,
        destination: String,
        departureDate: LocalDate,
        returnDate: LocalDate,
        numOfPassengers: Int
    ) = "origin=$origin&destination=$destination&departureDate=$departureDate" +
        "&returnDate=$returnDate&numberOfPassengers=$numOfPassengers"
}