package org.deblock.integration.remote.toughjet

import java.math.BigDecimal
import org.deblock.domain.price.BigDecimalUtils.percentage

object ToughJetDiscountedPriceCalculator {

    fun calculate(basePrice: BigDecimal, discount: Int, tax: BigDecimal): BigDecimal {
        require(basePrice >= BigDecimal.ZERO) {
            "BasePrice can't be negative"
        }
        require(discount >= 0) {
            "Discount can't be negative"
        }
        require(tax >= BigDecimal.ZERO) {
            "Tax can't be negative"
        }
        val discount = basePrice.percentage(discount)
        return basePrice.subtract(discount) + tax
    }
}