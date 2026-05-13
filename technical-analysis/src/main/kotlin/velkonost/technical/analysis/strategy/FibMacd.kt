package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.size
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal
import kotlin.math.min

class FibMacd(
    private val close: DataColumn<BigDecimal>,
    private val open: DataColumn<BigDecimal>,
    private val high: DataColumn<BigDecimal>,
    private val low: DataColumn<BigDecimal>,
    private val macdSignal: DataColumn<BigDecimal>,
    private val macd: DataColumn<BigDecimal>,
    private val ema200: DataColumn<BigDecimal>,
) : Strategy(StrategyType.FibMacd, close.size()) {

    var stopLossValue = BigDecimal.ZERO
    var takeProfitValue = BigDecimal.ZERO

    override fun atIndex(index: Int): StrategyDecision {
        var result = StrategyDecision.Nothing
        if (index <= 6) return StrategyDecision.Nothing

        // Record peaks and troughs in the last 'period' timesteps
        val period = min(size, 100)

        // Store peak values and their indices
        val closePeaks = mutableListOf<BigDecimal>()
        val locationPeaks = mutableListOf<Int>()

        // Store trough values and their indices
        val closeTroughs = mutableListOf<BigDecimal>()
        val locationTroughs = mutableListOf<Int>()

        // Find peaks & troughs in 'close' prices
        for (i in (index - period + 2) until (index - 2)) {
            if (i >= 2 && i < high.size() - 3) {
                if (high[i] > high[i - 1] && high[i] > high[i + 1] &&
                    high[i] > high[i - 2] && high[i] > high[i + 2]
                ) {
                    // Found a peak
                    closePeaks.add(high[i])
                    locationPeaks.add(i)
                } else if (low[i] < low[i - 1] && low[i] < low[i + 1] &&
                    low[i] < low[i - 2] && low[i] < low[i + 2]
                ) {
                    // Found a trough
                    closeTroughs.add(low[i])
                    locationTroughs.add(i)
                }
            }
        }

        // Determine the trend based on EMA200
        val trend = when {
            close[index] < ema200[index] -> 0 // Downtrend
            close[index] > ema200[index] -> 1 // Uptrend
            else -> -99 // Indeterminate
        }

        var maxPos = -99
        var minPos = -99

        if (trend == 1) {
            // Uptrend: find the start and end of the pullback
            var maxClose = BigDecimal("-999999")
            var minClose = BigDecimal("999999")

            var maxFlag = 0
            var minFlag = 0

            // Find the most recent peak (maxClose) after ignoring one peak
            for (i in closePeaks.size - 1 downTo 0) {
                if (closePeaks[i] > maxClose && maxFlag < 2) {
                    maxClose = closePeaks[i]
                    maxPos = locationPeaks[i]
                    maxFlag = 0
                } else if (maxFlag == 2) {
                    break
                } else {
                    maxFlag += 1
                }
            }

            // Find the corresponding trough (minClose) before the peak
            var startPoint = -1
            for (i in locationTroughs.indices) {
                if (locationTroughs[i] < maxPos) {
                    startPoint = i
                } else {
                    break
                }
            }

            if (startPoint >= 0) {
                for (i in startPoint downTo 0) {
                    if (closeTroughs[i] < minClose && minFlag < 2) {
                        minClose = closeTroughs[i]
                        minPos = locationTroughs[i]
                        minFlag = 0
                    } else if (minFlag == 2) {
                        break
                    } else {
                        minFlag += 1
                    }
                }
            }


            // Calculate Fibonacci levels
            val fibLevels = calculateFibLevels(maxClose, minClose, isUptrend = true)

            // Calculate Fibonacci retracement levels (extensions)
            val fibRetracements = calculateFibRetracements(maxClose, minClose, close[index], isUptrend = true)

            // Check for trade signals at each Fibonacci level
            for (level in 1 until fibLevels.size) {
                println("level: $level, index: $index")
                val condition1 = fibLevels[level - 1] > low[index - 2] && low[index - 2] > fibLevels[level]
                val condition2 = close[index - 3] > fibLevels[level]
                val condition3 = close[index - 4] > fibLevels[level]
                val condition4 = close[index - 6] > fibLevels[level]

                if (condition1 && condition2 && condition3 && condition4) {
                    // Bullish Engulfing Candle and MACD cross up
                    if (isBullishEngulfing(index, open, close) &&
                        isMacdCrossUp(index, macdSignal, macd)
                    ) {
                        result = StrategyDecision.Long
                        takeProfitValue = fibRetracements[level]
                        stopLossValue = close[index] - fibLevels[level] * BigDecimal("1.0001")
                        break
                    }
                }
            }
        } else if (trend == 0) {
            // Downtrend: find the start and end of the pullback
            var maxClose = BigDecimal("-999999")
            var minClose = BigDecimal("999999")

            var maxFlag = 0
            var minFlag = 0

            // Find the most recent trough (minClose) after ignoring one trough
            for (i in closeTroughs.size - 1 downTo 0) {
                if (closeTroughs[i] < minClose && minFlag < 2) {
                    minClose = closeTroughs[i]
                    minPos = locationTroughs[i]
                    minFlag = 0
                } else if (minFlag == 2) {
                    break
                } else {
                    minFlag += 1
                }
            }

            // Find the corresponding peak (maxClose) before the trough
            var startPoint = -1
            for (i in locationPeaks.indices) {
                if (locationPeaks[i] < minPos) {
                    startPoint = i
                } else {
                    break
                }
            }

            if (startPoint >= 0) {
                for (i in startPoint downTo 0) {
                    if (closePeaks[i] > maxClose && maxFlag < 2) {
                        maxClose = closePeaks[i]
                        maxPos = locationPeaks[i]
                        maxFlag = 0
                    } else if (maxFlag == 2) {
                        break
                    } else {
                        maxFlag += 1
                    }
                }
            }


            // Calculate Fibonacci levels
            val fibLevels = calculateFibLevels(maxClose, minClose, isUptrend = false)

            // Calculate Fibonacci retracement levels (extensions)
            val fibRetracements = calculateFibRetracements(maxClose, minClose, close[index], isUptrend = false)

            // Check for trade signals at each Fibonacci level
            for (level in 1 until fibLevels.size) {
                // Match the condition: fib_level_{n-1} < high[currentIndex - 2] < fib_level_n
                if (fibLevels[level - 1] < high[index - 2] && high[index - 2] < fibLevels[level] &&
                    close[index - 3] < fibLevels[level] &&
                    close[index - 4] < fibLevels[level] &&
                    close[index - 6] < fibLevels[level]
                ) {
                    // Bearish Engulfing Candle and MACD cross down
                    if (isBearishEngulfing(index, open, close) &&
                        isMacdCrossDown(index, macdSignal, macd)
                    ) {
                        result = StrategyDecision.Short
                        takeProfitValue = fibRetracements[level]
                        stopLossValue = fibLevels[level] * BigDecimal("1.0001") - close[index]
                        break
                    }
                }
            }
        }

        return result
    }

    private fun calculateFibLevels(
        maxClose: BigDecimal,
        minClose: BigDecimal,
        isUptrend: Boolean
    ): List<BigDecimal> {
        val levels = mutableListOf<BigDecimal>()
        val diff = maxClose - minClose

        if (isUptrend) {
            // For uptrend, levels are calculated from maxClose down to minClose
            levels.add(maxClose) // Level 0
            levels.add(maxClose - diff * BigDecimal("0.236")) // Level 1
            levels.add(maxClose - diff * BigDecimal("0.382")) // Level 2
            levels.add(maxClose - diff * BigDecimal("0.5"))   // Level 3
            levels.add(maxClose - diff * BigDecimal("0.618")) // Level 4
            levels.add(maxClose - diff * BigDecimal("0.786")) // Level 5
            levels.add(minClose) // Level 6
        } else {
            // For downtrend, levels are calculated from minClose up to maxClose
            levels.add(minClose) // Level 0
            levels.add(minClose + diff * BigDecimal("0.236")) // Level 1
            levels.add(minClose + diff * BigDecimal("0.382")) // Level 2
            levels.add(minClose + diff * BigDecimal("0.5"))   // Level 3
            levels.add(minClose + diff * BigDecimal("0.618")) // Level 4
            levels.add(minClose + diff * BigDecimal("0.786")) // Level 5
            levels.add(maxClose) // Level 6
        }

        return levels
    }

    private fun calculateFibRetracements(
        maxClose: BigDecimal,
        minClose: BigDecimal,
        currentClose: BigDecimal,
        isUptrend: Boolean
    ): List<BigDecimal> {
        val retracements = mutableListOf<BigDecimal>()
        val diff = maxClose - minClose

        if (isUptrend) {
            // For uptrend, target extensions above maxClose
            retracements.add(maxClose + diff * BigDecimal("1.236") - currentClose)
            retracements.add(maxClose + diff * BigDecimal("1.382") - currentClose)
            retracements.add(maxClose + diff * BigDecimal("1.5") - currentClose)
            retracements.add(maxClose + diff * BigDecimal("1.618") - currentClose)
            retracements.add(maxClose + diff * BigDecimal("1.786") - currentClose)
            retracements.add(maxClose + diff * BigDecimal("2") - currentClose)
        } else {
            // For downtrend, target extensions below minClose
            retracements.add(currentClose - (minClose - diff * BigDecimal("1.236")))
            retracements.add(currentClose - (minClose - diff * BigDecimal("1.382")))
            retracements.add(currentClose - (minClose - diff * BigDecimal("1.5")))
            retracements.add(currentClose - (minClose - diff * BigDecimal("1.618")))
            retracements.add(currentClose - (minClose - diff * BigDecimal("1.786")))
            retracements.add(currentClose - (minClose - diff * BigDecimal("2")))
        }

        return retracements
    }

    private fun isBullishEngulfing(
        index: Int,
        open: DataColumn<BigDecimal>,
        close: DataColumn<BigDecimal>
    ): Boolean {
        return close[index - 2] < open[index - 2] &&
                close[index - 1] > open[index - 1] &&
                close[index - 1] > close[index - 2] &&
                close[index] > close[index - 1]
    }

    private fun isBearishEngulfing(
        index: Int,
        open: DataColumn<BigDecimal>,
        close: DataColumn<BigDecimal>
    ): Boolean {
        return close[index - 2] > open[index - 2] &&
                close[index - 1] < open[index - 1] &&
                close[index - 1] < close[index - 2] &&
                close[index] < close[index - 1]
    }

    private fun isMacdCrossUp(
        index: Int,
        macdSignal: DataColumn<BigDecimal>,
        macd: DataColumn<BigDecimal>
    ): Boolean {
        return (macd[index - 1] < macdSignal[index - 1] ||
                macd[index - 2] < macdSignal[index - 2]) &&
                macd[index] > macdSignal[index]
    }

    private fun isMacdCrossDown(
        index: Int,
        macdSignal: DataColumn<BigDecimal>,
        macd: DataColumn<BigDecimal>
    ): Boolean {
        return (macd[index - 1] > macdSignal[index - 1] ||
                macd[index - 2] > macdSignal[index - 2]) &&
                macd[index] < macdSignal[index]
    }
}
