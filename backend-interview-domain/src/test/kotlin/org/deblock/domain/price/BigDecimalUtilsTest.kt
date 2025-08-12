package org.deblock.domain.price

import java.math.BigDecimal
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.deblock.domain.price.BigDecimalUtils.percentage
import org.junit.jupiter.api.Test

class BigDecimalUtilsTest {

    @Test
    fun `should return correct result in case of integer number input`() {
        // given
        val number = BigDecimal.valueOf(50L)

        // when
        val result = number.percentage(10)

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(5.0))
    }

    @Test
    fun `should return correct result in case of decimal number input`() {
        // given
        val number = BigDecimal.valueOf(5.5)

        // when
        val result = number.percentage(10)

        // then
        assertThat(result).isEqualTo(BigDecimal.valueOf(0.55))
    }

    @Test
    fun `should fail when number is negative`() {
        // given
        val number = BigDecimal.valueOf(-5.5)

        // when
        val block: () -> Unit = { number.percentage(10) }

        // then
        assertThatThrownBy(block)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Value must be positive")
    }

    @Test
    fun `should fail when percentage is negative`() {
        // given
        val number = BigDecimal.valueOf(5.5)

        // when
        val block: () -> Unit = { number.percentage(-10) }

        // then
        assertThatThrownBy(block)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Percent value must be positive")
    }

    @Test
    fun `should return 0 for 0 percent`() {
        // given
        val number = BigDecimal.valueOf(5.5)

        // when
        val result = number.percentage(0)

        // then
        assertThat(result.compareTo(BigDecimal.ZERO) == 0)
    }
}