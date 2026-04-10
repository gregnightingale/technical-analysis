package velkonost.technical.analysis.indicator.trend.sma

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal

class SmaFast(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 12,
    private val fillna: Boolean = false,
) : Indicator(IndicatorType.SmaFast, close.size()), SmaIndicator {

    override fun calculate(): DataColumn<BigDecimal> {
        val result = calculateSMA(close, window)
        return DataColumn.create(type.name, result.toList())
    }
}
