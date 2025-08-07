package org.deblock.org.deblock.app.controller

import org.deblock.domain.repository.FlightRepository
import org.deblock.org.deblock.app.mapper.FlightsRequestMapper.listFlightsQuery
import org.deblock.org.deblock.app.mapper.FlightsResponseMapper.listFlightsResponse
import org.deblock.org.deblock.app.request.ListFlightsRequest
import org.deblock.org.deblock.app.response.ListFlightsResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/flights")
class FlightsController(
    val flightRepository: FlightRepository,
) {

    @GetMapping
    @ResponseBody
    fun list(@ModelAttribute request: ListFlightsRequest): List<ListFlightsResponse> {
        val flights = flightRepository.getAll(listFlightsQuery(request))
        return listFlightsResponse(flights)
    }
}