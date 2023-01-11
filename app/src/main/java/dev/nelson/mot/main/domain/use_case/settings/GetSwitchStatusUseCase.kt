package dev.nelson.mot.main.domain.use_case.settings

import dev.nelson.mot.main.data.preferences.MotSwitch
import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.domain.use_case.UseCaseFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSwitchStatusUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseFlow<MotSwitch, Boolean> {

    override fun execute(params: MotSwitch): Flow<Boolean> {
        return settingsRepository.getSwitch(params)
    }
}
