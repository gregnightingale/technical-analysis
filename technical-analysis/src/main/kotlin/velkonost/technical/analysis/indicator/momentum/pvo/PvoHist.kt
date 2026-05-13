package velkonost.technical.analysis.indicator.momentum.pvo

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal

class PvoHist(
    private val volume: DataColumn<BigDecimal>,
    private val windowSlow: Int = 26,
    private val windowFast: Int = 12,
    private val windowSign: Int = 9,
    private val fillna: Boolean = false,
) : Indicator(IndicatorType.PvoHist, volume.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val pvo = Pvo(volume, windowSlow, windowFast, windowSign, fillna).invoke()
        val pvoSignal = PvoSignal(volume, windowSlow, windowFast, windowSign, fillna).invoke()
        val pvoHist = Array(volume.size()) { i ->
            pvo[i].subtract(pvoSignal[i])
        }
        return DataColumn.createValueColumn(type.name, pvoHist.toList())
    }
}
