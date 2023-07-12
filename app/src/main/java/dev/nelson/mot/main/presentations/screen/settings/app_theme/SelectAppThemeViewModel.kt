package dev.nelson.mot.main.presentations.screen.settings.app_theme

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.core.ui.model.MotAppTheme
import dev.nelson.mot.main.domain.use_case.base.execute
import dev.nelson.mot.main.domain.use_case.settings.GetAppThemeUseCase
import dev.nelson.mot.main.domain.use_case.settings.SetAppThemeUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectAppThemeViewModel @Inject constructor(
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val setAppThemeUseCase: SetAppThemeUseCase
) : BaseViewModel() {

    val selectAppViewState
        get() = _selectAppViewState.asStateFlow()
    private val _selectAppViewState = MutableStateFlow(SelectAppThemeViewState())

    init {
        launch {
            getAppThemeUseCase.execute()
                .collect {
                    _selectAppViewState.value = SelectAppThemeViewState(selectedAppTheme = it)
                }
        }
    }

    fun onAppThemeSelected(selectedTheme: MotAppTheme) = launch {
        setAppThemeUseCase.execute(selectedTheme)
    }
}
