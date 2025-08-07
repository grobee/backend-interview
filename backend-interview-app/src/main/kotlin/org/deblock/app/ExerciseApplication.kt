package org.deblock.org.deblock.app

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import org.deblock.domain.flightsupplier.FlightSupplier
import org.deblock.domain.repository.FlightRepository
import org.deblock.domain.repository.RemoteFlightRepository
import org.deblock.integration.remote.crazyair.CrazyAirFlightSupplier
import org.deblock.integration.remote.toughjet.ToughJetFlightSupplier
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestClient

@SpringBootApplication
class ExerciseApplication {

    @Bean("crazyAirHttpClient")
    fun crazyAirHttpClient(
        @Value("\${flightsupplier.crazyair.baseURL}") crazyAirBaseURL: URI,
    ): RestClient = RestClient.create(crazyAirBaseURL)

    @Bean("toughJetHttpClient")
    fun toughJetHttpClient(
        @Value("\${flightsupplier.toughjet.baseURL}") toughJetBaseURL: URI,
    ): RestClient = RestClient.create(toughJetBaseURL)
    
    @Bean
    fun flightSuppliers(
        @Qualifier("crazyAirHttpClient") crazyAirHttpClient: RestClient,
        @Qualifier("toughJetHttpClient") toughJetHttpClient: RestClient,
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
