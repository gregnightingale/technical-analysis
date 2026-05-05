package velkonost.technical.analysis.indicator.trend.ema

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal

class EmaSlow(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 26,
) : Indicator(IndicatorType.EmaSlow, close.size()) {

    override fun calculate(): DataColumn<BigDecimal> {
        val result = close.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}
