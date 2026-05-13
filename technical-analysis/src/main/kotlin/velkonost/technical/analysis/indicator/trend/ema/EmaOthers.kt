package velkonost.technical.analysis.indicator.trend.ema

import org.jetbrains.kotlinx.dataframe.DataColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal

class Ema3(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 3,
) : Indicator(IndicatorType.Ema3, close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val result = close.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}

class Ema6(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 6,
) : Indicator(IndicatorType.Ema6, close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val result = close.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}

class Ema8(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 8,
) : Indicator(IndicatorType.Ema8, close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val result = close.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}

class Ema9(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 9,
) : Indicator(IndicatorType.Ema9, close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val result = close.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}

class Ema14(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 14,
) : Indicator(IndicatorType.Ema14, close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val result = close.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}
class Ema20(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 20,
) : Indicator(IndicatorType.Ema20, close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val result = close.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}

class Ema50(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 50,
) : Indicator(IndicatorType.Ema50, close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val result = close.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}

class Ema100(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 100,
) : Indicator(IndicatorType.Ema100, close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val result = close.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}

class Ema200(
    private val close: DataColumn<BigDecimal>,
    private val window: Int = 200,
) : Indicator(IndicatorType.Ema200, close.size()) {

    override fun invoke(): DataColumn<BigDecimal> {
        val result = close.calculateEma(window)
        return DataColumn.createValueColumn(type.name, result.toList())
    }
}