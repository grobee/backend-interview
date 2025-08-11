package org.deblock.integration.remote.toughjet

import java.math.BigDecimal
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ToughJetDiscountedPriceCalculatorTest {

    @Test
    fun `should return discounted price result with added tax`() {
        // given
        val basePrice = BigDecimal.valueOf(1000.0)

        // when
        val result = ToughJetDiscountedPriceCalculator.calculate(
            basePrice = basePrice,
            discount = 10,
            tax = BigDecimal.valueOf(25.0),
        )

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(925.0))
    }
}