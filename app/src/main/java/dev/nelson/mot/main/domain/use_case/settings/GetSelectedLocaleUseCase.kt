package dev.nelson.mot.main.domain.use_case.settings

import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.domain.use_case.base.UseCaseFlow
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import javax.inject.Inject

class GetSelectedLocaleUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseFlow<Nothing?, Locale> {

    override fun execute(params: Nothing?): Flow<Locale> {
        return settingsRepository.getSelectedLocale()
    }
}
