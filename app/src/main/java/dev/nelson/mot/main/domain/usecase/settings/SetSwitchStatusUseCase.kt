package dev.nelson.mot.main.domain.usecase.settings

import dev.nelson.mot.main.data.preferences.MotSwitchType
import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.domain.usecase.base.UseCaseSuspend
import javax.inject.Inject

class SetSwitchStatusUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseSuspend<SetSwitchStatusParams, Unit> {

    override suspend fun execute(params: SetSwitchStatusParams) {
        settingsRepository.setSwitchState(params.motSwitchType, params.isEnabled)
    }
}

data class SetSwitchStatusParams(val motSwitchType: MotSwitchType, val isEnabled: Boolean)
