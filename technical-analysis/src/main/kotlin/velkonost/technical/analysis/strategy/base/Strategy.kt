package velkonost.technical.analysis.strategy.base

import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.api.toColumn

/**
 * Base abstract class for all trading strategies in the technical analysis library.
 * A strategy combines one or more technical indicators to generate trading signals.
 * Each strategy must implement the calculate() method to determine its trading decision.
 *
 * Key concepts:
 * - Strategies use technical indicators as building blocks
 * - Each strategy implements a specific trading logic
 * - Strategies generate clear buy/sell/hold signals
 * - Strategies can be combined to create more complex trading systems
 *
 * Usage:
 * 1. Extend this class for new strategies
 * 2. Implement the calculate() method with your strategy's logic
 * 3. Use technical indicators to analyze market conditions
 * 4. Return a clear trading decision (StrategyDecision)
 *
 * Best practices:
 * - Keep strategies focused on a single trading concept
 * - Document the strategy's logic and assumptions
 * - Consider market conditions and risk management
 * - Test strategies thoroughly before live trading
 *
 * @property type The name of the strategy, defined in StrategyName enum
 * @property scale The decimal scale used for calculations (default: 10)
 */
abstract class Strategy(
    val type: StrategyType,
    val size: Int,
    protected val scale: Int = 10
) {
    operator fun invoke(): DataColumn<StrategyDecision> =
        MutableList(size) { atIndex(it) }.toColumn(type.name)

    abstract fun atIndex(index: Int): StrategyDecision

    fun mostRecent(): StrategyDecision =
        atIndex(size - 1)
}
