package velkonost.technical.analysis.indicator.other

import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.api.columnOf
import org.jetbrains.kotlinx.dataframe.api.convertToBigDecimal
import org.jetbrains.kotlinx.dataframe.api.named
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import velkonost.technical.analysis.indicator.TestTechnicalAnalysis.closeColumn
import java.math.BigDecimal

class MaxClose30IndicatorTest {
    @Test
    fun test() {
        val prices: DataColumn<BigDecimal> = columnOf(10.0, 12.0, 15.0, 11.0, 14.0).named("Price").convertToBigDecimal()
        val result: DataColumn<BigDecimal> = MaxCloseIndicator( close = prices, window = 2 ).calculate()
        val expected: DataColumn<BigDecimal> = columnOf(10.0, 12.0, 15.0, 15.0, 14.0).named("Price").convertToBigDecimal()
        assertEquals(expected.toList(),result.toList())
    }
}
