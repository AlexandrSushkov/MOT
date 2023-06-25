package dev.nelson.mot.main.domain.use_case.settings

import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.domain.use_case.base.UseCaseSuspend
import dev.nelson.mot.core.ui.model.MotAppTheme
import javax.inject.Inject

class SetAppThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseSuspend<MotAppTheme, Unit> {
    override suspend fun execute(params: MotAppTheme) {
        settingsRepository.setAppTheme(params)
    }
}
