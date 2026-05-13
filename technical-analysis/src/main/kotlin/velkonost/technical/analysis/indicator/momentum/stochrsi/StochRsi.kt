package velkonost.technical.analysis.indicator.momentum.stochrsi

import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.api.mapIndexed
import velkonost.technical.analysis.extensions.calculateRollingMax
import velkonost.technical.analysis.extensions.calculateRollingMin
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import velkonost.technical.analysis.indicator.momentum.Rsi
import java.math.BigDecimal
import java.math.RoundingMode

class StochRsi(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 14,
    private val smooth1: Int = 3,
    private val smooth2: Int = 3,
    private val fillna: Boolean = false,
) : Indicator(IndicatorType.StochRsi, close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val rsi = Rsi(close, window, fillna = false).invoke()

        val lowestLowRsi = rsi.calculateRollingMin(window)
        val highestHighRsi = rsi.calculateRollingMax(window)

        val stochRsi = rsi.mapIndexed { index, currentRsi ->
            val lowest = lowestLowRsi[index]
            val highest = highestHighRsi[index]
            if (highest != lowest) {
                (currentRsi.subtract(lowest))
                    .divide(highest.subtract(lowest), scale, RoundingMode.HALF_UP)
            } else {
                BigDecimal.ZERO
            }
        }

        return DataColumn.createValueColumn(type.name, stochRsi.toList())
    }
}
