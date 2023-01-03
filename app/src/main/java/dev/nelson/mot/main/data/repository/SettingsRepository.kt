package dev.nelson.mot.main.data.repository

import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import dev.nelson.mot.main.BuildConfig
import dev.nelson.mot.main.data.preferences.MotSwitch
import dev.nelson.mot.main.data.preferences.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import javax.inject.Inject

class SettingsRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    fun getDownloadsDir(): File {
        return Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
    }

    /**
     * @return folder where database is stored.
     */
    fun getDataBaseDir(): File {
        val dataFolderPath = "${Environment.getDataDirectory().path}/$DATA_DIR_NAME"
        val appFolderName = BuildConfig.APPLICATION_ID
        return File("$dataFolderPath/$appFolderName/$DATABASES_DIR_NAME")
    }

    /**
     * Create copy of the file.
     *
     * @param from file to copy from.
     * @param to new file to be created.
     */
    fun copyFile(from: File, to: File) {
        val src: FileChannel = FileInputStream(from).channel
        val dst: FileChannel = FileOutputStream(to).channel
        dst.transferFrom(src, 0, src.size())
        src.close()
        dst.close()
    }

    suspend fun setSwitch(motSwitch: MotSwitch, isEnabled: Boolean) {
        dataStore.edit { preferences -> preferences[motSwitch.key] = isEnabled }
    }

    fun getSwitch(motSwitch: MotSwitch): Flow<Boolean> {
        return dataStore.data.map { it[motSwitch.key] ?: false }
    }

    companion object {
        const val DATA_DIR_NAME = "data"
        const val DATABASES_DIR_NAME = "databases"
    }

}
