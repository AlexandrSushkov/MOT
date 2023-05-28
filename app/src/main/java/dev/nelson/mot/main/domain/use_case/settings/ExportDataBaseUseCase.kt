package dev.nelson.mot.main.domain.use_case.settings

import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.db.MotDatabaseInfo
import dev.nelson.mot.main.domain.use_case.base.UseCaseSuspend
import java.io.File
import javax.inject.Inject

/**
 * Copy application data base to the Downloads folder.
 */
class ExportDataBaseUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseSuspend<Nothing?, Boolean> {

    override suspend fun execute(params: Nothing?): Boolean {
        return settingsRepository.backupDatabase()
    }
}
