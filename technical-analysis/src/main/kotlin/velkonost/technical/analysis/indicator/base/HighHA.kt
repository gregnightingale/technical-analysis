package velkonost.technical.analysis.indicator.base

import org.jetbrains.kotlinx.dataframe.DataColumn
import java.math.BigDecimal

class HighHA(
    private val openHA: DataColumn<BigDecimal>,
    private val high: DataColumn<BigDecimal>,
    private val closeHA: DataColumn<BigDecimal>,
) : Indicator(type = IndicatorType.HighHA, size = closeHA.size()) {
    override fun calculate(): DataColumn<BigDecimal> =
        DataColumn.createValueColumn(
            name = type.name,
            values = mutableListOf<BigDecimal>()
                .apply {
                    for (i in 0 until this@HighHA.size) {
                        add(
                            maxOf(
                                high[i],
                                openHA[i],
                                closeHA[i]
                            )
                        )
                    }
                }.toList()
        )
}