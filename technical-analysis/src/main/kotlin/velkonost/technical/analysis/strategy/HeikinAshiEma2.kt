package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal

class HeikinAshiEma2(
    private val openStreamH: DataColumn<BigDecimal>,
    private val highH: DataColumn<BigDecimal>,
    private val lowH: DataColumn<BigDecimal>,
    private val closeH: DataColumn<BigDecimal>,
    private val currentPos: Int = -99,
    private val fastd: DataColumn<BigDecimal>,
    private val fastk: DataColumn<BigDecimal>,
    private val ema200: DataColumn<BigDecimal>
) : Strategy(StrategyType.HeikinAshiEma2, closeH.size()) {

    override fun atIndex(index: Int): StrategyDecision {

        var tradeDirection = StrategyDecision.Nothing
        var closePos = 0
        val shortThreshold = BigDecimal("0.7")
        val longThreshold = BigDecimal("0.3")

        if (currentPos == -99) {
            // Check for SHORT trade opportunity
            if (index >= 1 &&
                fastk[index - 1] > fastd[index - 1] &&
                fastk[index] < fastd[index] &&
                closeH[index] < ema200[index]
            ) {
                outerLoop@ for (i in 10 downTo 3) {
                    val idxI = index - (i - 1)
                    if (idxI >= 0 &&
                        closeH[idxI] < openStreamH[idxI] &&
                        openStreamH[idxI] == highH[idxI]
                    ) {
                        for (j in i downTo 3) {
                            val idxJ = index - (j - 1)
                            val idxJPlus1 = idxJ + 1
                            if (idxJ >= 0 && idxJPlus1 < closeH.size() &&
                                ema200[idxJ] < closeH[idxJ] &&
                                closeH[idxJ] < openStreamH[idxJ] &&
                                closeH[idxJPlus1] < ema200[idxJPlus1]
                            ) {
                                var flag = true
                                for (r in j downTo 1) {
                                    val idxR = index - (r - 1)
                                    if (idxR >= 0 &&
                                        (fastd[idxR] < shortThreshold || fastk[idxR] < shortThreshold)
                                    ) {
                                        flag = false
                                        break
                                    }
                                }
                                if (flag) {
                                    tradeDirection = StrategyDecision.Short
                                    break@outerLoop
                                }
                            }
                        }
                    }
                }
            }
            // Check for LONG trade opportunity
            else if (index >= 1 &&
                fastk[index - 1] < fastd[index - 1] &&
                fastk[index] > fastd[index] &&
                closeH[index] > ema200[index]
            ) {
                outerLoop@ for (i in 10 downTo 3) {
                    val idxI = index - (i - 1)
                    if (idxI >= 0 &&
                        closeH[idxI] > openStreamH[idxI] &&
                        openStreamH[idxI] == lowH[idxI]
                    ) {
                        for (j in i downTo 3) {
                            val idxJ = index - (j - 1)
                            val idxJPlus1 = idxJ + 1
                            if (idxJ >= 0 && idxJPlus1 < closeH.size() &&
                                ema200[idxJ] > closeH[idxJ] &&
                                closeH[idxJ] > openStreamH[idxJ] &&
                                closeH[idxJPlus1] > ema200[idxJPlus1]
                            ) {
                                var flag = true
                                for (r in j downTo 1) {
                                    val idxR = index - (r - 1)
                                    if (idxR >= 0 &&
                                        (fastd[idxR] > longThreshold || fastk[idxR] > longThreshold)
                                    ) {
                                        flag = false
                                        break
                                    }
                                }
                                if (flag) {
                                    tradeDirection = StrategyDecision.Long
                                    break@outerLoop
                                }
                            }
                        }
                    }
                }
            }
        } else if (currentPos == 1 && closeH[index] < openStreamH[index]) {
            closePos = 1
        } else if (currentPos == 0 && closeH[index] > openStreamH[index]) {
            closePos = 1
        } else {
            closePos = 0
        }

        return tradeDirection
    }

}
