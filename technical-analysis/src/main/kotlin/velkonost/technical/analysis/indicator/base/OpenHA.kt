package velkonost.technical.analysis.indicator.base

import org.jetbrains.kotlinx.dataframe.DataColumn
import java.math.BigDecimal

class OpenHA(
    private val open: DataColumn<BigDecimal>,
    private val close: DataColumn<BigDecimal>,
    private val closeHA: DataColumn<BigDecimal>,
) : Indicator(type = IndicatorType.OpenHA, size = close.size()) {
    override fun calculate(): DataColumn<BigDecimal> =
        DataColumn.createValueColumn(
            name = type.name,
            values = mutableListOf<BigDecimal>()
                .apply {
                    for (i in 0 until this@OpenHA.size) {
                        if (i == 0) {
                            add((open[i] + close[i]) / BigDecimal(2))
                        } else {
                            add((this[i - 1] + closeHA[i - 1]) / BigDecimal(2))
                        }
                    }
                }.toList()
        )
}