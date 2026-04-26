package velkonost.technical.analysis.indicator.other

import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.api.max
import org.jetbrains.kotlinx.dataframe.api.min
import org.jetbrains.kotlinx.dataframe.api.toColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal

class MinCloseIndicator(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 30
) : Indicator(IndicatorType.MinClose, close.size()) {

    override fun calculate(): DataColumn<BigDecimal> {
        val output = close.toList().asReversed().windowed( size = window, partialWindows = true ) {
            it.min()
        }.asReversed()
        return output.toColumn(type.name)
    }
}
