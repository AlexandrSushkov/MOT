package dev.nelson.mot.main.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.provider.MediaStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dev.nelson.mot.db.MotDatabase
import dev.nelson.mot.db.MotDatabaseInfo
import dev.nelson.mot.main.BuildConfig
import dev.nelson.mot.main.data.preferences.MotSwitch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val motDatabase: MotDatabase,
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) {

    private val dataFolderPath: String
        get() = "${Environment.getDataDirectory().path}/$DATA_DIR_NAME"

    private val appFolderName: String
        get() = BuildConfig.APPLICATION_ID

    /**
     * @return folder where database is stored.
     */
    fun getDataBaseDir(): File {
        return File("$dataFolderPath/$appFolderName/$DATABASES_DIR_NAME")
    }

    fun getDataBaseTempDir(): File {
        return File("$dataFolderPath/$appFolderName/$DATABASES_TEMP_DIR_NAME")
    }

    fun copyFileFromUri(uri: Uri, to: File) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(to)
            if (inputStream != null) {
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
            }
        } catch (e: Exception) {
            // handle the exception here
        }
    }

    fun backupDatabase(): Boolean {
        val appDataBasesDir = getDataBaseDir()
        val downloadsDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
        if (downloadsDir.exists().not()) return false
        val appDataBaseFile = File(appDataBasesDir, MotDatabaseInfo.FILE_NAME)
        if (appDataBaseFile.exists().not()) return false
        if (motDatabase.isOpen) motDatabase.close()
        val backupDataBaseFileName = getDataBaseBackupUniqueName(downloadsDir, MotDatabaseInfo.BACKUP_FILE_NAME)
        val backupDatabaseFile = File(downloadsDir, backupDataBaseFileName)
        copyFile(appDataBaseFile, backupDatabaseFile)
        return true
    }

    suspend fun setSwitch(motSwitch: MotSwitch, isEnabled: Boolean) {
        dataStore.edit { preferences -> preferences[motSwitch.key] = isEnabled }
    }

    fun getSwitch(motSwitch: MotSwitch): Flow<Boolean> {
        return dataStore.data.map { it[motSwitch.key] ?: false }
    }

    private fun getDataBaseBackupUniqueName(directory: File, fileName: String): String {
        var uniqueFileName = fileName
        var count = 1
        while (File(directory, uniqueFileName).exists()) {
            val extension = uniqueFileName.substringAfterLast(DOT)
            val baseName = uniqueFileName.substringBeforeLast(DOT).substringBefore(OPEN_BRACKET)
            uniqueFileName = "$baseName($count).$extension"
            count++
        }
        return uniqueFileName
    }

    /**
     * Create copy of the file.
     *
     * @param from file to copy from.
     * @param to new file to be created.
     */
    private fun copyFile(from: File, to: File) {
        val src: FileChannel = FileInputStream(from).channel
        val dst: FileChannel = FileOutputStream(to).channel
        dst.transferFrom(src, 0, src.size())
        src.close()
        dst.close()
    }

    companion object {
        const val DATA_DIR_NAME = "data"
        const val DATABASES_DIR_NAME = "databases"
        const val DATABASES_TEMP_DIR_NAME = "databases-temp"
        const val DOT = "."
        const val OPEN_BRACKET = "("
    }
}
