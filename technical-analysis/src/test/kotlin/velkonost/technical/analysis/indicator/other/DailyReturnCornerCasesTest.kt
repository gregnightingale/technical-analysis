package velkonost.technical.analysis.indicator.other

import org.jetbrains.kotlinx.dataframe.DataColumn
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DailyReturnCornerCasesTest {

    @Test
    fun `Empty input returns single zero`() {
        val close = DataColumn.createValueColumn("close", emptyList<BigDecimal>())
        val indicator = DailyReturnIndicator(close)
        val result = indicator.calculate()
        Assertions.assertEquals(0, result.size())
    }

    @Test
    fun `Zero previous value yields zero return`() {
        val close = DataColumn.createValueColumn(
            "close",
            listOf(BigDecimal.ZERO, BigDecimal("100"))
        )
        val indicator = DailyReturnIndicator(close)
        val result = indicator.calculate().toList()
        // First is 0 by definition, second is 0 because previous is zero
        Assertions.assertEquals(listOf(BigDecimal.ZERO, BigDecimal.ZERO), result)
    }
}
