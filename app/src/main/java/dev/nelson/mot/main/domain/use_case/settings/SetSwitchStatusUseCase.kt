package dev.nelson.mot.main.domain.use_case.settings

import dev.nelson.mot.main.data.preferences.MotSwitch
import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.domain.use_case.base.UseCaseSuspend
import javax.inject.Inject

class SetSwitchStatusUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseSuspend<SetSwitchStatusParams, Unit> {

    override suspend fun execute(params: SetSwitchStatusParams) {
        settingsRepository.setSwitch(params.motSwitch, params.isEnabled)
    }
}

data class SetSwitchStatusParams(val motSwitch: MotSwitch, val isEnabled: Boolean)
