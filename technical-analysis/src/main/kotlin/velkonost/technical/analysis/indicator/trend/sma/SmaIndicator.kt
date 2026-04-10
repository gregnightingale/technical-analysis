package velkonost.technical.analysis.indicator.trend.sma

import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.indices
import java.math.BigDecimal
import java.math.RoundingMode

internal interface SmaIndicator {

    fun calculateSMA(close: DataColumn<BigDecimal>, window: Int): Array<BigDecimal> {
        val closeList = close.toList()
        val smaValues = Array<BigDecimal>(close.size()) { BigDecimal.ZERO }
        val size = close.size()

        // Optimization: Using a cumulative sum to reduce the number of operations.
        for (i in 0 until size) {
            val startIndex = maxOf(0, i - window + 1)
            var sum = BigDecimal.ZERO
            val windowSize = i - startIndex + 1
            
            // Summing the values in the window
            for (j in startIndex..i) {
                sum = sum.add(closeList[j])
            }
            
            smaValues[i] = sum.divide(BigDecimal(windowSize), 10, RoundingMode.HALF_UP)
        }

        return smaValues
    }
}
