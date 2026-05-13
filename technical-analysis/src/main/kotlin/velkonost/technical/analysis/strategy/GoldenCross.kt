package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal

class GoldenCross(
    private val close: DataColumn<BigDecimal>,
    private val ema100: DataColumn<BigDecimal>,
    private val ema50: DataColumn<BigDecimal>,
    private val ema20: DataColumn<BigDecimal>,
    private val rsi: DataColumn<BigDecimal>
) : Strategy(StrategyType.GoldenCross, close.size()) {
    
    override fun atIndex(index: Int): StrategyDecision {

        if (index < 3 ||
            index >= close.size() ||
            index >= ema100.size() ||
            index >= ema50.size() ||
            index >= ema20.size() ||
            index >= rsi.size()
        ) {
            return StrategyDecision.Nothing
        }

        try {
            val closeCurrent = close[index]
            val ema100Current = ema100[index]
            val ema50Current = ema50[index]
            val ema20Current = ema20[index]
            val rsiCurrent = rsi[index]

            // Long Entry Conditions
            if (closeCurrent > ema100Current && rsiCurrent > BigDecimal(50)) {
                val crossUpOccurred = (
                        (ema20[index - 1] < ema50[index - 1] && ema20Current > ema50Current) ||
                                (ema20[index - 2] < ema50[index - 2] && ema20Current > ema50Current) ||
                                (ema20[index - 3] < ema50[index - 3] && ema20Current > ema50Current)
                        )
                if (crossUpOccurred) {
                    return StrategyDecision.Long
                }
            }
            // Short Entry Conditions
            else if (closeCurrent < ema100Current && rsiCurrent < BigDecimal(50)) {
                val crossDownOccurred = (
                        (ema20[index - 1] > ema50[index - 1] && ema20Current < ema50Current) ||
                                (ema20[index - 2] > ema50[index - 2] && ema20Current < ema50Current) ||
                                (ema20[index - 3] > ema50[index - 3] && ema20Current < ema50Current)
                        )
                if (crossDownOccurred) {
                    return StrategyDecision.Short
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return StrategyDecision.Nothing
        }

        return StrategyDecision.Nothing
    }

}
