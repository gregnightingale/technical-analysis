package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal

class Breakout(
    private val close: DataColumn<BigDecimal>,
    private val volume: DataColumn<BigDecimal>,
    private val maxClose: DataColumn<BigDecimal>,
    private val minClose: DataColumn<BigDecimal>,
    private val maxVolume: DataColumn<BigDecimal>,
    private val invert: Boolean = false
) : Strategy(StrategyType.Breakout, close.size()) {

    override fun calculateAtIndex(index: Int): StrategyDecision {

        if (index < 0 ||
            index >= close.size() ||
            index >= volume.size() ||
            index >= maxClose.size() ||
            index >= minClose.size() ||
            index >= maxVolume.size()
        ) {
            return StrategyDecision.Nothing
        }

        return if (invert) {
            when {
                close[index] >= maxClose[index] && volume[index] >= maxVolume[index] -> {
                    StrategyDecision.Short
                }

                close[index] <= minClose[index] && volume[index] >= maxVolume[index] -> {
                    StrategyDecision.Long
                }

                else -> {
                    StrategyDecision.Nothing
                }
            }
        } else {
            when {
                close[index] >= maxClose[index] && volume[index] >= maxVolume[index] -> {
                    StrategyDecision.Long
                }

                close[index] <= minClose[index] && volume[index] >= maxVolume[index] -> {
                    StrategyDecision.Short
                }

                else -> {
                    StrategyDecision.Nothing
                }
            }
        }
    }
}
