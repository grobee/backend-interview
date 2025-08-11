package org.deblock.domain.model

@JvmInline
value class AirportCode(
    val value: String,
) {

    init {
        check(value.length == 3) {
            "AirportCode must be a 3 letter IATA code"
        }
    }
}