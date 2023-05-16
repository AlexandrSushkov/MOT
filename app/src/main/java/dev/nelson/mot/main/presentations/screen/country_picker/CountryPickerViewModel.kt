package dev.nelson.mot.main.presentations.screen.country_picker

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.domain.use_case.settings.ExportDataBaseUseCase
import dev.nelson.mot.main.domain.use_case.settings.GetSelectedLocaleUseCase
import dev.nelson.mot.main.domain.use_case.settings.GetSwitchStatusUseCase
import dev.nelson.mot.main.domain.use_case.settings.ImportDataBaseUseCase
import dev.nelson.mot.main.domain.use_case.settings.SetLocaleUseCase
import dev.nelson.mot.main.domain.use_case.settings.SetSwitchStatusUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.extention.doesSearchMatch
import dev.nelson.mot.main.util.extention.filterDefaultCountries
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CountryPickerViewModel @Inject constructor(
    getSwitchStatusUseCase: GetSwitchStatusUseCase,
    getSelectedLocaleUseCase: GetSelectedLocaleUseCase,
    private val exportDataBaseUseCase: ExportDataBaseUseCase,
    private val importDataBaseUseCase: ImportDataBaseUseCase,
    private val setSwitchStatusUseCase: SetSwitchStatusUseCase,
    private val setLocaleUseCase: SetLocaleUseCase,
) : BaseViewModel() {

    // actions
    val closeScreenAction
        get() = _closeScreenAction.asSharedFlow()
    private val _closeScreenAction = MutableSharedFlow<Unit>()

    // states
    private val _countriesPickerState =
        MutableStateFlow(Locale.getAvailableLocales().filterDefaultCountries())

    val searchText
        get() = _searchText.asStateFlow()
    private val _searchText = MutableStateFlow(StringUtils.EMPTY)

    val countriesPickerState = _searchText
        .debounce(300)
        .combine(_countriesPickerState) { searchText, countries ->
            countries.filter { it.doesSearchMatch(searchText) }
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            _countriesPickerState.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onLocaleSelected(locale: Locale) {
        launch {
            setLocaleUseCase.execute(locale)
            _closeScreenAction.emit(Unit)
        }
    }
}
