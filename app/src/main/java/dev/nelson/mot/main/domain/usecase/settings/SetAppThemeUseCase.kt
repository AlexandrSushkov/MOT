package dev.nelson.mot.main.domain.usecase.settings

import dev.nelson.mot.core.ui.model.MotAppTheme
import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.domain.usecase.base.UseCaseSuspend
import javax.inject.Inject

class SetAppThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseSuspend<MotAppTheme, Unit> {

    override suspend fun execute(params: MotAppTheme) {
        settingsRepository.setAppTheme(params)
    }
}
