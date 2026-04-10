package velkonost.technical.analysis.indicator.other

import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.api.max
import org.jetbrains.kotlinx.dataframe.api.toColumn
import velkonost.technical.analysis.indicator.base.Indicator
import velkonost.technical.analysis.indicator.base.IndicatorType
import java.math.BigDecimal

class MaxVolumeIndicator(
    private val volume: DataColumn<BigDecimal>,
) : Indicator(IndicatorType.MaxVolume, volume.size()) {

    override fun calculate(): DataColumn<BigDecimal> =
        List(size) { volume.max() }.toColumn(type.name)
}
