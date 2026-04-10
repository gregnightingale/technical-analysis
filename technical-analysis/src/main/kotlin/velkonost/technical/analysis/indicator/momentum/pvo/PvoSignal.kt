package velkonost.technical.analysis.indicator.momentum.pvo

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal

class PvoSignal(
    private val volume: DataColumn<BigDecimal>,
    private val windowSlow: Int = 26,
    private val windowFast: Int = 12,
    private val windowSign: Int = 9,
    private val fillna: Boolean = false,
) : Indicator(IndicatorType.PvoSignal, volume.size()) {

    override fun calculate(): DataColumn<BigDecimal> {
        val pvo = Pvo(volume, windowSlow, windowFast, windowSign, fillna).calculate()
        val pvoSignal = pvo.calculateEma(windowSign)

        return DataColumn.create(type.name, pvoSignal.toList())
    }
}
