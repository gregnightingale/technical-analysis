package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal

class EmaCrossover(
    private val emaShort: DataColumn<BigDecimal>,
    private val emaLong: DataColumn<BigDecimal>,
) : Strategy(StrategyType.EmaCrossover, emaShort.size()) {

    override fun atIndex(index: Int): StrategyDecision {

        if (index < 1 ||
            index >= emaShort.size() ||
            index >= emaLong.size()
        ) {
            return StrategyDecision.Nothing
        }

        // Previous EMA values
        val emaShortPrev = emaShort[index - 1]
        val emaLongPrev = emaLong[index - 1]

        // Current EMA values
        val emaShortCurrent = emaShort[index]
        val emaLongCurrent = emaLong[index]

        return when {
            emaShortPrev > emaLongPrev && emaShortCurrent < emaLongCurrent -> {
                StrategyDecision.Short
            }

            emaShortPrev < emaLongPrev && emaShortCurrent > emaLongCurrent -> {
                StrategyDecision.Long
            }

            else -> {
                StrategyDecision.Nothing
            }
        }
    }
}
