package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal

class HeikinAshiEma(
    private val openStreamH: DataColumn<BigDecimal>,
    private val closeH: DataColumn<BigDecimal>,
    private val fastd: DataColumn<BigDecimal>,
    private val fastk: DataColumn<BigDecimal>,
    private val ema200: DataColumn<BigDecimal>,
    private var closePos: Int = 0,
    private var currentPos: Int = -99,
) : Strategy(StrategyType.HeikinAshiEma, closeH.size()) {

    override fun atIndex(index: Int): StrategyDecision {

        var updatedTradeDirection = StrategyDecision.Nothing
        var updatedClosePos = closePos

        if (currentPos == -99) {
            updatedTradeDirection = StrategyDecision.Nothing

            // Thresholds
            val shortThreshold = BigDecimal("0.8")
            val longThreshold = BigDecimal("0.2")

            // Look for Shorts
            if (fastk[index] > shortThreshold && fastd[index] > shortThreshold) {
                // Check the last 10 candles
                for (i in 10 downTo 3) {
                    val idx = index - i
                    if (idx < 0) continue

                    if (fastd[idx] >= shortThreshold && fastk[idx] >= shortThreshold) {
                        // Look for a cross within the next few candles
                        for (j in i downTo 3) {
                            val crossIdx = index - j
                            if (crossIdx < 1) continue

                            if (fastk[crossIdx] > fastd[crossIdx] && fastk[crossIdx + 1] < fastd[crossIdx + 1]) {
                                var flag = true
                                for (r in crossIdx downTo (index - j + 1)) {
                                    if (fastk[r] < shortThreshold || fastd[r] < shortThreshold) {
                                        flag = false
                                        break
                                    }
                                }

                                if (flag &&
                                    closeH[index - 2] > ema200[index - 2] &&
                                    closeH[index - 1] < ema200[index - 1]
                                ) {
                                    if (closeH[index] < openStreamH[index]) {
                                        // Bearish candle
                                        updatedTradeDirection = StrategyDecision.Short
                                    }
                                    break
                                } else {
                                    break
                                }
                            }
                        }
                    }
                }
            }

            // Look for Longs
            if (fastk[index] < longThreshold && fastd[index] < longThreshold) {
                // Check the last 10 candles
                for (i in 10 downTo 3) {
                    val idx = index - i
                    if (idx < 0) continue

                    if (fastd[idx] <= longThreshold && fastk[idx] <= longThreshold) {
                        // Look for a cross within the next few candles
                        for (j in i downTo 3) {
                            val crossIdx = index - j
                            if (crossIdx < 1) continue

                            if (fastk[crossIdx] < fastd[crossIdx] && fastk[crossIdx + 1] > fastd[crossIdx + 1] &&
                                fastk[index] < longThreshold && fastd[index] < longThreshold
                            ) {
                                var flag = true
                                for (r in crossIdx downTo (index - j + 1)) {
                                    if (fastk[r] > longThreshold || fastd[r] > longThreshold) {
                                        flag = false
                                        break
                                    }
                                }

                                if (flag &&
                                    closeH[index - 2] < ema200[index - 2] &&
                                    closeH[index - 1] > ema200[index - 1]
                                ) {
                                    if (closeH[index] > openStreamH[index]) {
                                        // Bullish candle
                                        updatedTradeDirection = StrategyDecision.Long
                                    }
                                    break
                                } else {
                                    break
                                }
                            }
                        }
                    }
                }
            }
        } else {
            when (currentPos) {
                1 -> {
                    if (closeH[index] < openStreamH[index]) {
                        updatedClosePos = 1
                    }
                }
                0 -> {
                    if (closePos.toBigDecimal() > openStreamH[index]) {
                        updatedClosePos = 1
                    }
                }
                else -> {
                    updatedClosePos = 0
                }
            }
        }

        return updatedTradeDirection
    }

}
