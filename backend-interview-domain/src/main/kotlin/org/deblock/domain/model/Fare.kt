package org.deblock.domain.model

import java.math.BigDecimal

@JvmInline
value class Fare(
    val value: BigDecimal,
) : Comparable<Fare> {

    init {
        require(value >= BigDecimal.ZERO) { "Value of fare can't be negative" }
    }

    override fun compareTo(other: Fare): Int = value.compareTo(other.value)
}