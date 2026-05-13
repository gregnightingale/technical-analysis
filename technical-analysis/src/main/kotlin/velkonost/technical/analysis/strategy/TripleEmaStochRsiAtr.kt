package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal

class TripleEmaStochRsiAtr(
    private val close: DataColumn<BigDecimal>,
    private val ema50: DataColumn<BigDecimal>,
    private val ema14: DataColumn<BigDecimal>,
    private val ema8: DataColumn<BigDecimal>,
    private val fastd: DataColumn<BigDecimal>,
    private val fastk: DataColumn<BigDecimal>
) : Strategy(StrategyType.TripleEmaStochRsiAtr, close.size()) {

    override fun atIndex(index: Int): StrategyDecision {

        // Ensure indices are valid and avoid IndexOutOfBoundsException
        if (index < 1 ||
            index >= close.size() ||
            index >= ema50.size() ||
            index >= ema14.size() ||
            index >= ema8.size() ||
            index >= fastd.size() ||
            index >= fastk.size()
        ) {
            return StrategyDecision.Nothing
        }

        // Get current and previous values
        val closeCurrent = close[index]
        val ema8Current = ema8[index]
        val ema14Current = ema14[index]
        val ema50Current = ema50[index]
        val fastkCurrent = fastk[index]
        val fastdCurrent = fastd[index]

        val fastkPrev = fastk[index - 1]
        val fastdPrev = fastd[index - 1]

        // Buy Signal
        val isBuySignal = closeCurrent > ema8Current && ema8Current > ema14Current && ema14Current > ema50Current &&
                fastkCurrent > fastdCurrent && fastkPrev < fastdPrev

        if (isBuySignal) {
            return StrategyDecision.Long
        }

        // Sell Signal
        val isSellSignal = closeCurrent < ema8Current && ema8Current < ema14Current && ema14Current < ema50Current &&
                fastkCurrent < fastdCurrent && fastkPrev > fastdPrev

        if (isSellSignal) {
            return StrategyDecision.Short
        }

        return StrategyDecision.Nothing
    }

}
