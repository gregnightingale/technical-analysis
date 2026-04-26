package velkonost.technical.analysis.indicator.other

import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.api.columnOf
import org.jetbrains.kotlinx.dataframe.api.convertToBigDecimal
import org.jetbrains.kotlinx.dataframe.api.max
import org.jetbrains.kotlinx.dataframe.api.named
import org.jetbrains.kotlinx.dataframe.api.toColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal
import org.jetbrains.kotlinx.dataframe.api.toDataFrame

class MaxCloseIndicator(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 30
) : Indicator(IndicatorType.MaxClose, close.size()) {

    override fun calculate(): DataColumn<BigDecimal> {
        val output = close.toList().asReversed().windowed( size = window, partialWindows = true ) {
           it.max()
        }.asReversed()
        return output.toColumn(type.name)
    }
}
