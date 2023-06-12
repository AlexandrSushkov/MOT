package dev.nelson.mot.main.domain.use_case.settings

import dev.nelson.mot.core.ui.model.MotAppTheme
import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.domain.use_case.base.UseCaseFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseFlow<Nothing?, MotAppTheme> {

    override fun execute(params: Nothing?): Flow<MotAppTheme> {
        return settingsRepository.getAppTheme()
    }
}
