package velkonost.technical.analysis.indicator.base

import org.jetbrains.kotlinx.dataframe.DataColumn
import java.math.BigDecimal

class LowHA(
    private val openHA: DataColumn<BigDecimal>,
    private val low: DataColumn<BigDecimal>,
    private val closeHA: DataColumn<BigDecimal>,
) : Indicator(type = IndicatorType.LowHA, size = closeHA.size()) {

    override fun invoke(): DataColumn<BigDecimal> =
        DataColumn.createValueColumn(
            name = type.name,
            values = mutableListOf<BigDecimal>()
                .apply {
                    for (i in 0 until this@LowHA.size) {
                        add(
                            minOf(
                                low[i],
                                openHA[i],
                                closeHA[i]
                            )
                        )
                    }
                }.toList()
        )
}