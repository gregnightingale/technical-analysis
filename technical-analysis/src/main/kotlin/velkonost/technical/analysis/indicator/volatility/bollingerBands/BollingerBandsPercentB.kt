package velkonost.technical.analysis.indicator.volatility.bollingerBands

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal
import java.math.RoundingMode

class BollingerBandsPercentB(
    private val close: DataColumn<BigDecimal>,
    private val upperBand: DataColumn<BigDecimal>,
    private val lowerBand: DataColumn<BigDecimal>
) : Indicator( IndicatorType.BbPercent, close.size()){

    override fun invoke(): DataColumn<BigDecimal> {
        val result = close.toList().mapIndexed { index, close ->
            val hBandValue = upperBand[index]
            val lBandValue = lowerBand[index]
            val denominator = hBandValue.subtract(lBandValue)
            if (denominator.compareTo(BigDecimal.ZERO) != 0) {
                close.subtract(lBandValue)
                    .divide(denominator, 10, RoundingMode.HALF_UP)
            } else {
                BigDecimal.ZERO
            }
        }
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}