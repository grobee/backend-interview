package org.deblock.org.deblock.app

import org.deblock.domain.flightsupplier.FlightSupplier
import org.deblock.domain.http.HttpClient
import org.deblock.domain.repository.FlightRepository
import org.deblock.domain.repository.RemoteFlightRepository
import org.deblock.integration.remote.crazyair.CrazyAirFlightSupplier
import org.deblock.integration.remote.toughjet.ToughJetFlightSupplier
import org.deblock.org.deblock.app.http.WebFluxHttpClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class ExerciseApplication {

    @Bean("crazyAirHttpClient")
    fun crazyAirHttpClient(
        @Value("\${flightsupplier.crazyair.baseURL}") crazyAirBaseURL: String,
    ): HttpClient = WebFluxHttpClient(WebClient.create(crazyAirBaseURL))

    @Bean("toughJetHttpClient")
    fun toughJetHttpClient(
        @Value("\${flightsupplier.toughjet.baseURL}") toughJetBaseURL: String,
    ): HttpClient = WebFluxHttpClient(WebClient.create(toughJetBaseURL))

    @Bean
    fun flightSuppliers(
        @Qualifier("crazyAirHttpClient") crazyAirHttpClient: HttpClient,
        @Qualifier("toughJetHttpClient") toughJetHttpClient: HttpClient,
    ): List<FlightSupplier> = listOf(
        CrazyAirFlightSupplier(crazyAirHttpClient),
        ToughJetFlightSupplier(toughJetHttpClient),
    )

    @Bean
    fun flightRepository(flightSuppliers: List<FlightSupplier>): FlightRepository =
        RemoteFlightRepository(flightSuppliers)
}

fun main(args: Array<String>) {
    runApplication<ExerciseApplication>(*args)
}
