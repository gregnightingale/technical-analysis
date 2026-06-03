package velkonost.technical.analysis.indicator.trend.ema

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import velkonost.technical.analysis.indicator.TestTechnicalAnalysis

class EmaFast1212Test {
    @Test
    fun test() {
        with(TestTechnicalAnalysis) {
            val indicator = EmaFast12(
                close = closeColumn,
            )
            assertEquals(true, indicator.isEqual(dataframe))
        }
    }
}
