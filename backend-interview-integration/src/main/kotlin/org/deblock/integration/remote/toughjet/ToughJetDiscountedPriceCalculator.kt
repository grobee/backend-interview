package org.deblock.integration.remote.toughjet

import java.math.BigDecimal
import org.deblock.domain.price.BigDecimalUtils.percentage

object ToughJetDiscountedPriceCalculator {

    fun calculate(basePrice: BigDecimal, discount: Int, tax: BigDecimal): BigDecimal {
        val discount = basePrice.percentage(discount)
        return basePrice.subtract(discount) + tax
    }
}