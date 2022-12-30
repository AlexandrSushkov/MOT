package dev.nelson.mot.main.domain.use_case.settings

import dev.nelson.mot.main.data.preferences.MotSwitch
import dev.nelson.mot.main.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSwitchStatusUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend fun execute(motSwitch: MotSwitch): Flow<Boolean> {
        return settingsRepository.getSwitch(motSwitch)
    }
}
