package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal

class StochBb(
    private val fastd: DataColumn<BigDecimal>,
    private val fastk: DataColumn<BigDecimal>,
    private val percentB: DataColumn<BigDecimal>
) : Strategy(StrategyType.StochasticBb, fastd.size()) {

    override fun atIndex(index: Int): StrategyDecision {

        // Проверка валидности индексов
        if (index < 2 ||
            index >= fastd.size() ||
            index >= fastk.size() ||
            index >= percentB.size()
        ) {
            return StrategyDecision.Nothing
        }

        val percentB1 = percentB[index]
        val percentB2 = percentB[index - 1]
        val percentB3 = percentB[index - 2]

        val fastkCurrent = fastk[index]
        val fastdCurrent = fastd[index]
        val fastkPrev = fastk[index - 1]
        val fastdPrev = fastd[index - 1]

        return when {
            fastkCurrent < BigDecimal("0.2") && fastdCurrent < BigDecimal("0.2") &&
                    fastkCurrent > fastdCurrent && fastkPrev < fastdPrev &&
                    (percentB1 < BigDecimal.ZERO || percentB2 < BigDecimal.ZERO || percentB3 < BigDecimal.ZERO) -> {
                StrategyDecision.Long
            }

            fastkCurrent > BigDecimal("0.8") && fastdCurrent > BigDecimal("0.8") &&
                    fastkCurrent < fastdCurrent && fastkPrev > fastdPrev &&
                    (percentB1 > BigDecimal.ONE || percentB2 > BigDecimal.ONE || percentB3 > BigDecimal.ONE) -> {
                StrategyDecision.Short
            }

            else -> StrategyDecision.Nothing
        }
    }

}
