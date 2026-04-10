package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal

class EmaCross(
    private val emaShort: DataColumn<BigDecimal>,
    private val emaLong: DataColumn<BigDecimal>
) : Strategy(StrategyType.EmaCross, emaShort.size()) {

    override fun calculateAtIndex(index: Int): StrategyDecision {

        if (index !in 4..<size || index >= size) {
            return StrategyDecision.Nothing
        }

        if (emaShort[index - 4] > emaLong[index - 4] &&
            emaShort[index - 3] > emaLong[index - 3] &&
            emaShort[index - 2] > emaLong[index - 2] &&
            emaShort[index - 1] > emaLong[index - 1] &&
            emaShort[index] < emaLong[index]
        ) {
            return StrategyDecision.Short
        }

        if (emaShort[index - 4] < emaLong[index - 4] &&
            emaShort[index - 3] < emaLong[index - 3] &&
            emaShort[index - 2] < emaLong[index - 2] &&
            emaShort[index - 1] < emaLong[index - 1] &&
            emaShort[index] > emaLong[index]
        ) {
            return StrategyDecision.Long
        }

        return StrategyDecision.Nothing

    }
}
