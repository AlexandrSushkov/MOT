package dev.nelson.mot.main.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.room.Room
import dev.nelson.mot.BuildConfig
import dev.nelson.mot.db.MIGRATION_1_2
import dev.nelson.mot.db.MotDatabase
import dev.nelson.mot.db.MotDatabaseInfo
import dev.nelson.mot.main.data.preferences.MotSwitchType
import dev.nelson.mot.main.data.preferences.PreferencesKeys
import dev.nelson.mot.core.ui.model.MotAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.util.Locale
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

    /**
     * Check integrity of the database from a database file.
     * @return true if the given database pass integrity_check, false otherwise.
     */
    fun checkDataBaseIntegrity(file: File): Boolean {
        val database =
            Room.databaseBuilder(context, MotDatabase::class.java, MotDatabaseInfo.FILE_NAME)
                .createFromFile(file)
                .addMigrations(MIGRATION_1_2)
                .allowMainThreadQueries()
                .build()

        return database.openHelper
            .readableDatabase
            .isDatabaseIntegrityOk
            .also { database.close() }
    }

    fun backupDatabase(): Boolean {
        val appDataBasesDir = getDataBaseDir()
        val downloadsDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
        if (downloadsDir.exists().not()) return false
        val appDataBaseFile = File(appDataBasesDir, MotDatabaseInfo.FILE_NAME)
        if (appDataBaseFile.exists().not()) return false
        if (motDatabase.isOpen) motDatabase.close()
        val backupDataBaseFileName =
            getDataBaseBackupUniqueName(downloadsDir, MotDatabaseInfo.BACKUP_FILE_NAME)
        val backupDatabaseFile = File(downloadsDir, backupDataBaseFileName)
        copyFile(appDataBaseFile, backupDatabaseFile)
        return true
    }

    suspend fun setSwitchState(motSwitchType: MotSwitchType, isEnabled: Boolean) {
        dataStore.edit { preferences -> preferences[motSwitchType.key] = isEnabled }
    }

    fun getSwitchState(motSwitchType: MotSwitchType): Flow<Boolean> {
        val defaultValue = motSwitchType is MotSwitchType.ShowCents
        return dataStore.data.map { it[motSwitchType.key] ?: defaultValue }
    }

    fun getSelectedLocale(): Flow<Locale> {
        return dataStore.data.map { it[PreferencesKeys.SELECTED_COUNTRY_CODE] }
            .map { countryCode -> countryCode?.let { Locale("en", it) } ?: Locale.getDefault() }
    }

    suspend fun setLocale(locale: Locale) {
        dataStore.edit { it[PreferencesKeys.SELECTED_COUNTRY_CODE] = locale.country }
    }

    fun getAppTheme(): Flow<MotAppTheme> {
        return dataStore.data.map { it[PreferencesKeys.APP_THEME] }
            .map { appThemeString ->
                appThemeString?.let { MotAppTheme.fromString(it) } ?: MotAppTheme.default
            }
    }

    suspend fun setAppTheme(theme: MotAppTheme) {
        dataStore.edit { it[PreferencesKeys.APP_THEME] = theme.name }
    }

    private fun getDataBaseBackupUniqueName(directory: File, fileName: String): String {
        var uniqueFileName = fileName
        var count = 1
        while (File(directory, uniqueFileName).exists()) {
            val extension = uniqueFileName.substringAfterLast(DOT)
            val baseName = uniqueFileName.substringBeforeLast(DOT).substringBefore(OPEN_BRACKET)
            uniqueFileName =
                "$baseName$OPEN_BRACKET$count$CLOSE_BRACKET$DOT$extension" // pattern: fileName(1).ext
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
        const val CLOSE_BRACKET = ")"
    }
}
