package dev.nelson.mot.main.domain.usecase.settings

import dev.nelson.mot.main.data.preferences.MotSwitchType
import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.domain.usecase.base.UseCaseFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSwitchStatusUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseFlow<MotSwitchType, Boolean> {

    override fun execute(params: MotSwitchType): Flow<Boolean> {
        return settingsRepository.getSwitchState(params)
    }
}
