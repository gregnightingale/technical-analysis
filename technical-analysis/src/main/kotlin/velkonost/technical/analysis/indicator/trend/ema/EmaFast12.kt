package velkonost.technical.analysis.indicator.trend.ema

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal

class EmaFast12(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 12,
) : Indicator(IndicatorType.EmaFast12, close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val result = close.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}
