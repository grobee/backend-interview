package org.deblock.integration.remote.toughjet

import java.math.BigDecimal
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
        assertThat(result.compareTo(BigDecimal.valueOf(925.00)) == 0)
    }

    @Test
    fun `should return price result with added tax when no discount`() {
        // given
        val basePrice = BigDecimal.valueOf(1000.0)

        // when
        val result = ToughJetDiscountedPriceCalculator.calculate(
            basePrice = basePrice,
            discount = 0,
            tax = BigDecimal.valueOf(25.0),
        )

        // then
        assertThat(result.compareTo(BigDecimal.valueOf(1025.00)) == 0)
    }

    @Test
    fun `should return price result with no tax and no discount`() {
        // given
        val basePrice = BigDecimal.valueOf(1000.0)

        // when
        val result = ToughJetDiscountedPriceCalculator.calculate(
            basePrice = basePrice,
            discount = 0,
            tax = BigDecimal.ZERO,
        )

        // then
        assertThat(result.compareTo(BigDecimal.valueOf(1000.00)) == 0)
    }

    @Test
    fun `should throw exception when basePrice is negative`() {
        // given
        val basePrice = BigDecimal.valueOf(-1000.0)

        // when
        val block: () -> Unit = {
            ToughJetDiscountedPriceCalculator.calculate(
                basePrice = basePrice,
                discount = 10,
                tax = BigDecimal.valueOf(25.0),
            )
        }

        // then
        assertThatThrownBy(block)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("BasePrice can't be negative")
    }

    @Test
    fun `should throw exception when discount is negative`() {
        // given
        val basePrice = BigDecimal.valueOf(1000.0)

        // when
        val block: () -> Unit = {
            ToughJetDiscountedPriceCalculator.calculate(
                basePrice = basePrice,
                discount = -10,
                tax = BigDecimal.valueOf(25.0),
            )
        }

        // then
        assertThatThrownBy(block)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Discount can't be negative")
    }

    @Test
    fun `should throw exception when tax is negative`() {
        // given
        val basePrice = BigDecimal.valueOf(1000.0)

        // when
        val block: () -> Unit = {
            ToughJetDiscountedPriceCalculator.calculate(
                basePrice = basePrice,
                discount = 10,
                tax = BigDecimal.valueOf(-25.0),
            )
        }

        // then
        assertThatThrownBy(block)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Tax can't be negative")
    }
}