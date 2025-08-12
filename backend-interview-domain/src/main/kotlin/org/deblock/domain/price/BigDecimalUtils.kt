package org.deblock.domain.price

import java.math.BigDecimal

object BigDecimalUtils {

    fun BigDecimal.percentage(percent: Int): BigDecimal {
        require(this >= BigDecimal.ZERO) {
            "Value must be positive"
        }
        require(percent >= 0) {
            "Percent value must be positive"
        }
        val fraction = BigDecimal.valueOf(percent / 100.0)
        return multiply(fraction)
    }
}