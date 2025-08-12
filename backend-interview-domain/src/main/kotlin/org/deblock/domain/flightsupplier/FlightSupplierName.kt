package org.deblock.domain.flightsupplier

@JvmInline
value class FlightSupplierName(
    val value: String,
) {

    init {
        require(value.isNotBlank()) {
            "FlightSupplierName value can't be blank"
        }
    }

    override fun toString() = value
}