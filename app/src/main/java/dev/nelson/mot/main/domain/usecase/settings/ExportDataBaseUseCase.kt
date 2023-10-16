package dev.nelson.mot.main.domain.usecase.settings

import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.domain.usecase.base.UseCaseSuspend
import javax.inject.Inject

/**
 * Copy application database to the Downloads folder.
 */
class ExportDataBaseUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseSuspend<Nothing?, Boolean> {

    override suspend fun execute(params: Nothing?): Boolean {
        return settingsRepository.backupDatabase()
    }
}
