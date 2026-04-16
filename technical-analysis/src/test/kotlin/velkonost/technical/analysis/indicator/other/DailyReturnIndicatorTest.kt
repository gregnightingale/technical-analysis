package velkonost.technical.analysis.indicator.other

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import velkonost.technical.analysis.indicator.TestTechnicalAnalysis

class DailyReturnIndicatorTest {
    @Test
    fun test() {
        with(TestTechnicalAnalysis) {
            val indicator = DailyReturnIndicator(
                close = closeColumn,
                fillna = true
            )
            Assertions.assertEquals(true, indicator.isEqual(dataframe))
        }
    }
}
