package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal

class CandleWick(
    private val close: DataColumn<BigDecimal>,
    private val open: DataColumn<BigDecimal>,
    private val high: DataColumn<BigDecimal>,
    private val low: DataColumn<BigDecimal>
) : Strategy(StrategyType.CandleWick, close.size()) {

    override fun calculateAtIndex(index: Int): StrategyDecision {

        if (index < 4 ||
            index >= close.size() ||
            index >= open.size() ||
            index >= high.size() ||
            index >= low.size()
        ) {
            return StrategyDecision.Nothing  // Index out of bounds or insufficient data
        }

        // Retrieve necessary values for calculations
        val closeMinus4 = close[index - 4]
        val closeMinus3 = close[index - 3]
        val closeMinus2 = close[index - 2]
        val closeMinus1 = close[index - 1]
        val openMinus1 = open[index - 1]
        val highMinus1 = high[index - 1]
        val lowMinus1 = low[index - 1]
        val closeCurrent = close[index]

        if (
            closeMinus4 < closeMinus3
            && closeMinus3 < closeMinus2
            && closeMinus1 < openMinus1
            && highMinus1.subtract(openMinus1)
                .add(closeMinus1.subtract(lowMinus1)) > BigDecimal.TEN.multiply(openMinus1.subtract(closeMinus1))
            && closeCurrent < closeMinus1
        ) {
            return StrategyDecision.Short
        }

        if (
            closeMinus4 > closeMinus3
            && closeMinus3 > closeMinus2
            && closeMinus1 > openMinus1
            && highMinus1.subtract(closeMinus1)
                .add(openMinus1.subtract(lowMinus1)) > BigDecimal.TEN.multiply(closeMinus1.subtract(openMinus1))
            && closeCurrent > closeMinus1
        ) {
            return StrategyDecision.Long
        }

        return StrategyDecision.Nothing
    }

}
