package velkonost.technical.analysis.indicator.base

import org.jetbrains.kotlinx.dataframe.DataColumn
import java.math.BigDecimal

class CloseHA(
    private val open: DataColumn<BigDecimal>,
    private val high: DataColumn<BigDecimal>,
    private val low: DataColumn<BigDecimal>,
    private val close: DataColumn<BigDecimal>,
) : Indicator(type = IndicatorType.CloseHA, size = close.size()) {

    override fun invoke(): DataColumn<BigDecimal> =
        DataColumn.createValueColumn(
            name = type.name,
            values = mutableListOf<BigDecimal>()
                .apply {
                    for (i in 0 until this@CloseHA.size) {
                        add(
                            (open[i] + high[i] + low[i] + close[i]) / BigDecimal(4)
                        )
                    }
                }.toList()
        )
}