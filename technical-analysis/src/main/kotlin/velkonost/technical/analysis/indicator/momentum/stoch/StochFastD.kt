package velkonost.technical.analysis.indicator.momentum.stoch

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal

class StochFastD(
    private val stochFastK: DataColumn<BigDecimal>,
    private val window: Int = 3
) : Indicator(IndicatorType.StochFastD, size = stochFastK.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val result = stochFastK.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}