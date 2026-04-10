package velkonost.technical.analysis.strategy

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.strategy.base.Strategy
import velkonost.technical.analysis.strategy.base.StrategyDecision
import velkonost.technical.analysis.strategy.base.StrategyType
import java.math.BigDecimal
import kotlin.math.max

class RsiStochEma(
    private val close: DataColumn<BigDecimal>,
    private val ema200: DataColumn<BigDecimal>,
    private val rsiSignal: DataColumn<BigDecimal>,
    private val fastk: DataColumn<BigDecimal>,
    private val fastd: DataColumn<BigDecimal>,
    private val backStep: Int = 0
) : Strategy(StrategyType.RsiStochEma, close.size()) {

    private enum class SignalType {
        None,
        BearishDivergence,
        BullishDivergence
    }

    override fun calculateAtIndex(index: Int): StrategyDecision {

        // Проверка валидности индексов
        if (index < 4 ||
            index >= close.size() ||
            index >= ema200.size() ||
            index >= rsiSignal.size() ||
            index >= fastk.size() ||
            index >= fastd.size()
        ) {
            return StrategyDecision.Nothing
        }

        var signal1 = SignalType.None
        val period = 60

        val peaksRsi = mutableListOf<BigDecimal>()
        val correspondingClosePeaks = mutableListOf<BigDecimal>()
        val locationPeaks = mutableListOf<Int>()

        val troughsRsi = mutableListOf<BigDecimal>()
        val correspondingCloseTroughs = mutableListOf<BigDecimal>()
        val locationTroughs = mutableListOf<Int>()

        val start = max(2, index - period)
        val end = index

        // Поиск пиков и впадин в RSI
        for (i in start..end) {
            if (i - 2 >= 0 && i + 2 <= index) {
                val rsiCurrent = rsiSignal[i]
                val rsiPrev1 = rsiSignal[i - 1]
                val rsiNext1 = rsiSignal[i + 1]
                val rsiPrev2 = rsiSignal[i - 2]
                val rsiNext2 = rsiSignal[i + 2]

                // Проверка на пик
                if (rsiCurrent > rsiPrev1 && rsiCurrent > rsiNext1 &&
                    rsiCurrent > rsiPrev2 && rsiCurrent > rsiNext2
                ) {
                    peaksRsi.add(rsiCurrent)
                    correspondingClosePeaks.add(close[i])
                    locationPeaks.add(i)
                }
                // Проверка на впадину
                else if (rsiCurrent < rsiPrev1 && rsiCurrent < rsiNext1 &&
                    rsiCurrent < rsiPrev2 && rsiCurrent < rsiNext2
                ) {
                    troughsRsi.add(rsiCurrent)
                    correspondingCloseTroughs.add(close[i])
                    locationTroughs.add(i)
                }
            }
        }

        // Проверка на дивергенции
        if (peaksRsi.size >= 2) {
            val lastPeakIndex = peaksRsi.size - 1
            val prevPeakIndex = peaksRsi.size - 2

            val lastPeakRsi = peaksRsi[lastPeakIndex]
            val prevPeakRsi = peaksRsi[prevPeakIndex]
            val lastPeakPrice = correspondingClosePeaks[lastPeakIndex]
            val prevPeakPrice = correspondingClosePeaks[prevPeakIndex]

            // Скрытая медвежья дивергенция: цена выше, RSI ниже
            if (lastPeakRsi < prevPeakRsi &&
                lastPeakPrice > prevPeakPrice &&
                prevPeakRsi.subtract(lastPeakRsi) > BigDecimal.ONE
            ) {
                signal1 = SignalType.BearishDivergence
            }
        }

        if (troughsRsi.size >= 2) {
            val lastTroughIndex = troughsRsi.size - 1
            val prevTroughIndex = troughsRsi.size - 2

            val lastTroughRsi = troughsRsi[lastTroughIndex]
            val prevTroughRsi = troughsRsi[prevTroughIndex]
            val lastTroughPrice = correspondingCloseTroughs[lastTroughIndex]
            val prevTroughPrice = correspondingCloseTroughs[prevTroughIndex]

            // Скрытая бычья дивергенция: цена ниже, RSI выше
            if (lastTroughRsi > prevTroughRsi &&
                lastTroughPrice < prevTroughPrice &&
                lastTroughRsi.subtract(prevTroughRsi) > BigDecimal.ONE
            ) {
                signal1 = SignalType.BullishDivergence
            }
        }

        // Проверка на достаточность данных для индикаторов FastK и FastD
        if (index < 2) {
            return StrategyDecision.Nothing
        }

        when (signal1) {
            SignalType.BullishDivergence -> {
                if (fastk[index] > fastd[index] &&
                    (fastk[index - 1] < fastd[index - 1] || fastk[index - 2] < fastd[index - 2]) &&
                    close[index] > ema200[index]
                ) {
                    return StrategyDecision.Long
                }
            }

            SignalType.BearishDivergence -> {
                if (fastk[index] < fastd[index] &&
                    (fastk[index - 1] > fastd[index - 1] || fastk[index - 2] > fastd[index - 2]) &&
                    close[index] < ema200[index]
                ) {
                    return StrategyDecision.Short
                }
            }

            SignalType.None -> {
                return StrategyDecision.Nothing
            }
        }

        return StrategyDecision.Nothing
    }

}
