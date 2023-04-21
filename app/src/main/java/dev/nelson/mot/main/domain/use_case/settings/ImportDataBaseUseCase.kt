package dev.nelson.mot.main.domain.use_case.settings

import android.net.Uri
import dev.nelson.mot.db.MotDatabaseInfo
import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.domain.use_case.base.UseCaseSuspend
import java.io.File
import javax.inject.Inject

class ImportDataBaseUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : UseCaseSuspend<ImportDataBaseParams, Boolean> {
    @Throws(FileAlreadyExistException::class)
    override suspend fun execute(params: ImportDataBaseParams): Boolean {
        val appDataBasesDir = settingsRepository.getDataBaseDir()
        val tempDataBaseDir = settingsRepository.getDataBaseTempDir().apply {
            mkdir()
        }
        val tempDataBaseFile = File(tempDataBaseDir, MotDatabaseInfo.FILE_NAME)
        settingsRepository.copyFileFromUri(params.uri, tempDataBaseFile)
        try {
            val isIntegrityCheckPassed = settingsRepository.checkDataBaseIntegrity(tempDataBaseFile)
            if (isIntegrityCheckPassed) {
                appDataBasesDir.deleteRecursively()
                val newDataBaseDir = settingsRepository.getDataBaseDir()
                tempDataBaseDir.renameTo(newDataBaseDir)
                return true
            }
        } catch (e: Exception) {
            tempDataBaseDir.deleteRecursively()
            throw e
        }
        tempDataBaseDir.deleteRecursively()
        return false
    }
}

data class ImportDataBaseParams(
    val uri: Uri,
    val isOverride: Boolean = false
)
