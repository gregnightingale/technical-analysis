package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal

class TripleEma(
    private val ema3: DataColumn<BigDecimal>,
    private val ema6: DataColumn<BigDecimal>,
    private val ema9: DataColumn<BigDecimal>
) : Strategy(StrategyType.TripleEma, ema3.size()) {

    override fun atIndex(index: Int): StrategyDecision {

        // Проверка валидности индексов
        if (index < 4 ||
            index >= ema3.size() ||
            index >= ema6.size() ||
            index >= ema9.size()
        ) {
            return StrategyDecision.Nothing
        }

        // Проверка условий для сигнала на продажу (Short)
        val isShortSignal = (4 downTo 1).all { i ->
            ema3[index - i] > ema6[index - i] &&
                    ema3[index - i] > ema9[index - i]
        } && ema3[index] < ema6[index] &&
                ema3[index] < ema9[index]

        if (isShortSignal) {
            return StrategyDecision.Short
        }

        // Проверка условий для сигнала на покупку (Long)
        val isLongSignal = (4 downTo 1).all { i ->
            ema3[index - i] < ema6[index - i] &&
                    ema3[index - i] < ema9[index - i]
        } && ema3[index] > ema6[index] &&
                ema3[index] > ema9[index]

        if (isLongSignal) {
            return StrategyDecision.Long
        }

        return StrategyDecision.Nothing
    }

}
