package dev.nelson.mot.main.domain.usecase.price

import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.data.preferences.MotSwitchType
import dev.nelson.mot.main.domain.usecase.base.UseCaseFlow
import dev.nelson.mot.main.domain.usecase.base.execute
import dev.nelson.mot.main.domain.usecase.settings.GetSelectedLocaleUseCase
import dev.nelson.mot.main.domain.usecase.settings.GetSwitchStatusUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetPriceViewStateUseCase @Inject constructor(
    private val getSwitchStatusUseCase: GetSwitchStatusUseCase,
    private val getSelectedLocaleUseCase: GetSelectedLocaleUseCase
) : UseCaseFlow<Nothing?, PriceViewState> {

    override fun execute(params: Nothing?): Flow<PriceViewState> {
        return combine(
            getSelectedLocaleUseCase.execute(),
            getSwitchStatusUseCase.execute(MotSwitchType.ShowCents),
            getSwitchStatusUseCase.execute(MotSwitchType.ShowCurrencySymbol),
            getSwitchStatusUseCase.execute(MotSwitchType.ShowDigits)
        ) { locale, showCents, showCurrencySymbol, hideDigits ->
            PriceViewState(
                locale = locale,
                isShowCents = showCents,
                isShowCurrencySymbol = showCurrencySymbol,
                isShowDigits = hideDigits
            )
        }
    }
}
