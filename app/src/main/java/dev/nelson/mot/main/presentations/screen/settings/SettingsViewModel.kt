package dev.nelson.mot.main.presentations.screen.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.domain.use_case.ExportDataBaseUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val exportDataBaseUseCase: ExportDataBaseUseCase
) : BaseViewModel() {

    fun onExportDataBaseClick() {
        viewModelScope.launch {
            val isExported = exportDataBaseUseCase.execute()
            val toastMessage = if (isExported) DATA_BASE_EXPORTED_SUCCESSFULLY else DATA_BASE_EXPORT_FAILED
            showToast(toastMessage)
        }
    }

    companion object {
        const val DATA_BASE_EXPORTED_SUCCESSFULLY = "data base was exported successfully. Please, check Downloads folder."
        const val DATA_BASE_EXPORT_FAILED = "Oops! data base export failed."
    }
}
