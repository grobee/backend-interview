package org.deblock.domain.price

import java.math.BigDecimal

object BigDecimalUtils {

    fun BigDecimal.percentage(percent: Int): BigDecimal {
        val fraction = BigDecimal.valueOf(percent / 100.0)
        return multiply(fraction)
    }
}