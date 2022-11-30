package dev.nelson.mot.main.domain.use_case

import dev.nelson.mot.main.data.repository.SettingsRepository
import dev.nelson.mot.main.data.room.MotDatabaseInfo
import java.io.File
import javax.inject.Inject

/**
 * Copy application data base to the Downloads folder.
 */
class ExportDataBaseUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    fun execute(): Boolean {
        val dataBasesDir = settingsRepository.getDataBaseDir()
        val downloadsDir = settingsRepository.getDownloadsDir()
        val dbFile = File(dataBasesDir, MotDatabaseInfo.NAME)
        val dbBackupFile = File(downloadsDir, MotDatabaseInfo.BACKUP_NAME)
        if (downloadsDir.exists().not()) return false
        if (dbFile.exists().not()) return false
        settingsRepository.copyFile(dbFile, dbBackupFile)
        return true
    }
}
