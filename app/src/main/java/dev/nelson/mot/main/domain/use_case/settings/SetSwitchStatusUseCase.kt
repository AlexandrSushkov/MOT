package dev.nelson.mot.main.domain.use_case.settings

import dev.nelson.mot.main.data.preferences.MotSwitch
import dev.nelson.mot.main.data.repository.SettingsRepository
import javax.inject.Inject

class SetSwitchStatusUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend fun execute(motSwitch: MotSwitch, isEnabled: Boolean) {
        settingsRepository.setSwitch(motSwitch, isEnabled)
    }
}