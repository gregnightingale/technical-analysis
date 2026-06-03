package velkonost.technical.analysis.indicator.trend.ema

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import velkonost.technical.analysis.indicator.TestTechnicalAnalysis

class EmaSlow2626Test {
    @Test
    fun test() {
        with(TestTechnicalAnalysis) {
            val indicator = EmaSlow26(
                close = closeColumn,
            )
            assertEquals(true, indicator.isEqual(dataframe))
        }
    }
}
