package velkonost.technical.analysis.indicator.trend.sma

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal

class SmaSlow(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 26,
    private val fillna: Boolean = false,
) : Indicator(IndicatorType.SmaSlow, close.size()), SmaIndicator {

    override fun calculate(): DataColumn<BigDecimal> {
        val result = calculateSMA(close, window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}
