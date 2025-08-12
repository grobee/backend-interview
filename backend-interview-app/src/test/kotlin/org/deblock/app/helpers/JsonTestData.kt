package org.deblock.org.deblock.app.helpers

import org.deblock.org.deblock.app.helpers.ResourceReader.readResourceAsString

object JsonTestData {

    const val EMPTY_JSON_BODY = "[]"


    object Full {
        val crazyAirResponse = readResourceAsString("json/full/crazy-air.json")
        val toughJetResponse = readResourceAsString("json/full/tough-jet.json")
        val expectedResponse = readResourceAsString("json/full/response.json")
    }

    object Partial {
        val crazyAirResponse = readResourceAsString("json/partial/crazy-air.json")
        val expectedResponse = readResourceAsString("json/partial/response.json")
    }
}