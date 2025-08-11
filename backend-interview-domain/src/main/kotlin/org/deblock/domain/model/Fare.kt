package org.deblock.domain.model

import java.math.BigDecimal

@JvmInline
value class Fare(
    val value: BigDecimal,
) : Comparable<Fare> {

    override fun compareTo(other: Fare): Int = value.compareTo(other.value)
}