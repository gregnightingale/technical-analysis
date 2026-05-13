package velkonost.technical.analysis.indicator.momentum.ppo

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal
import java.math.RoundingMode

class Ppo(
    private val close: DataColumn<BigDecimal>,
    private val windowSlow: Int = 26,
    private val windowFast: Int = 12,
    private val windowSign: Int = 9,
    private val fillna: Boolean = false,
) : Indicator(IndicatorType.Ppo, size = close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        // Calculating Fast and Slow EMAs
        val emaFast = close.calculateEma(windowFast)
        val emaSlow = close.calculateEma(windowSlow)

        val ppo = Array(close.size()) { i ->
            if (emaSlow[i] != BigDecimal.ZERO) {
                (emaFast[i].subtract(emaSlow[i]))
                    .divide(emaSlow[i], scale, RoundingMode.HALF_UP)
                    .multiply(BigDecimal(100))
            } else BigDecimal.ZERO
        }
        return DataColumn.createValueColumn(type.name, ppo.toList())
    }
}
