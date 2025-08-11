package org.deblock.domain.price

import java.math.BigDecimal
import org.assertj.core.api.Assertions.assertThat
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
}