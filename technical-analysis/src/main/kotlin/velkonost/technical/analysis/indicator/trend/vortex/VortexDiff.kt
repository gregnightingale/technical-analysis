package velkonost.technical.analysis.indicator.trend.vortex

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal

class VortexDiff(
    private val high: DataColumn<BigDecimal>,
    private val low: DataColumn<BigDecimal>,
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 14,
    private val fillna: Boolean = false,
) : Indicator(IndicatorType.VortexIndDiff, close.size()), VortexIndicator {

    override fun invoke(): DataColumn<BigDecimal> {
        val vip = VortexPositive(high, low, close, window, fillna).invoke()
        val vin = VortexNegative(high, low, close, window, fillna).invoke()

        val diff = vip.toList().zip(vin.toList()) { vipValue, vinValue -> vipValue.subtract(vinValue) }
        return DataColumn.createValueColumn(type.name, diff)
    }
}
