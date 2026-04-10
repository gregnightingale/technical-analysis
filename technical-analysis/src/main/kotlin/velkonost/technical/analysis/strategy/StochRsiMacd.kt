package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal

class StochRsiMacd(
    private val fastd: DataColumn<BigDecimal>,
    private val fastk: DataColumn<BigDecimal>,
    private val rsi: DataColumn<BigDecimal>,
    private val macd: DataColumn<BigDecimal>,
    private val macdSignal: DataColumn<BigDecimal>,
) : Strategy(StrategyType.StochRsiMacd, fastd.size()) {

    override fun calculateAtIndex(index: Int): StrategyDecision {

        // Check for valid index boundaries
        if (index < 3 ||
            index >= fastd.size() ||
            index >= fastk.size() ||
            index >= rsi.size() ||
            index >= macd.size() ||
            index >= macdSignal.size()
        ) {
            return StrategyDecision.Nothing
        }

        try {
            val fastdCurrent = fastd[index]
            val fastkCurrent = fastk[index]
            val rsiCurrent = rsi[index]
            val macdCurrent = macd[index]
            val macdSignalCurrent = macdSignal[index]

            // Long Conditions
            val longCondition1 = (
                    fastdCurrent < BigDecimal(20) &&
                            fastkCurrent < BigDecimal(20) &&
                            rsiCurrent > BigDecimal(50) &&
                            macdCurrent > macdSignalCurrent &&
                            macd[index - 1] < macdSignal[index - 1]
                    )

            val longCondition2 = (
                    fastd[index - 1] < BigDecimal(20) &&
                            fastk[index - 1] < BigDecimal(20) &&
                            rsiCurrent > BigDecimal(50) &&
                            macdCurrent > macdSignalCurrent &&
                            macd[index - 2] < macdSignal[index - 2] &&
                            fastdCurrent < BigDecimal(80) &&
                            fastkCurrent < BigDecimal(80)
                    )

            val longCondition3 = (
                    fastd[index - 2] < BigDecimal(20) &&
                            fastk[index - 2] < BigDecimal(20) &&
                            rsiCurrent > BigDecimal(50) &&
                            macdCurrent > macdSignalCurrent &&
                            macd[index - 1] < macdSignal[index - 1] &&
                            fastdCurrent < BigDecimal(80) &&
                            fastkCurrent < BigDecimal(80)
                    )

            val longCondition4 = (
                    fastd[index - 3] < BigDecimal(20) &&
                            fastk[index - 3] < BigDecimal(20) &&
                            rsiCurrent > BigDecimal(50) &&
                            macdCurrent > macdSignalCurrent &&
                            macd[index - 2] < macdSignal[index - 2] &&
                            fastdCurrent < BigDecimal(80) &&
                            fastkCurrent < BigDecimal(80)
                    )

            if (longCondition1 || longCondition2 || longCondition3 || longCondition4) {
                return StrategyDecision.Long
            }

            // Short Conditions
            val shortCondition1 = (
                    fastdCurrent > BigDecimal(80) &&
                            fastkCurrent > BigDecimal(80) &&
                            rsiCurrent < BigDecimal(50) &&
                            macdCurrent < macdSignalCurrent &&
                            macd[index - 1] > macdSignal[index - 1]
                    )

            val shortCondition2 = (
                    fastd[index - 1] > BigDecimal(80) &&
                            fastk[index - 1] > BigDecimal(80) &&
                            rsiCurrent < BigDecimal(50) &&
                            macdCurrent < macdSignalCurrent &&
                            macd[index - 2] > macdSignal[index - 2] &&
                            fastdCurrent > BigDecimal(20) &&
                            fastkCurrent > BigDecimal(20)
                    )

            val shortCondition3 = (
                    fastd[index - 2] > BigDecimal(80) &&
                            fastk[index - 2] > BigDecimal(80) &&
                            rsiCurrent < BigDecimal(50) &&
                            macdCurrent < macdSignalCurrent &&
                            macd[index - 1] > macdSignal[index - 1] &&
                            fastdCurrent > BigDecimal(20) &&
                            fastkCurrent > BigDecimal(20)
                    )

            val shortCondition4 = (
                    fastd[index - 3] > BigDecimal(80) &&
                            fastk[index - 3] > BigDecimal(80) &&
                            rsiCurrent < BigDecimal(50) &&
                            macdCurrent < macdSignalCurrent &&
                            macd[index - 2] > macdSignal[index - 2] &&
                            fastdCurrent > BigDecimal(20) &&
                            fastkCurrent > BigDecimal(20)
                    )

            if (shortCondition1 || shortCondition2 || shortCondition3 || shortCondition4) {
                return StrategyDecision.Short
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return StrategyDecision.Nothing
        }

        return StrategyDecision.Nothing
    }

}
