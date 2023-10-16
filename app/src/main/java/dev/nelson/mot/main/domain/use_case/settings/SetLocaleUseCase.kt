package dev.nelson.mot.main.domain.use_case.settings

import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.domain.use_case.base.UseCaseSuspend
import java.util.Locale
import javax.inject.Inject

class SetLocaleUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseSuspend<Locale, Unit> {
    override suspend fun execute(params: Locale) {
        settingsRepository.setLocale(params)
    }
}
