package org.deblock.org.deblock.app

import java.time.Duration
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
import reactor.util.retry.Retry

@SpringBootApplication
class ExerciseApplication {

    @Bean
    fun httpRetryConfig(): Retry = Retry.fixedDelay(3, Duration.ofMillis(50))

    @Bean("crazyAirHttpClient")
    fun crazyAirHttpClient(
        @Value("\${flightsupplier.crazyair.baseURL}") crazyAirBaseURL: String,
        httpRetryConfig: Retry,
    ): HttpClient = WebFluxHttpClient(WebClient.create(crazyAirBaseURL), httpRetryConfig)

    @Bean("toughJetHttpClient")
    fun toughJetHttpClient(
        @Value("\${flightsupplier.toughjet.baseURL}") toughJetBaseURL: String,
        httpRetryConfig: Retry,
    ): HttpClient = WebFluxHttpClient(WebClient.create(toughJetBaseURL), httpRetryConfig)

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
