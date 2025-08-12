package org.deblock.domain.model

@JvmInline
value class Airline(
    val value: String,
) {

    init {
        require(value.isNotBlank()) {
            "Airline value can't be blank"
        }
    }
}
