package dev.nelson.mot.main.presentations.screen.country_picker

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.domain.use_case.settings.SetLocaleUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.extention.doesSearchMatch
import dev.nelson.mot.main.util.extention.filterDefaultCountries
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CountryPickerViewModel @Inject constructor(
    private val setLocaleUseCase: SetLocaleUseCase,
) : BaseViewModel() {

    // actions
    val closeScreenAction
        get() = _closeScreenAction.asSharedFlow()
    private val _closeScreenAction = MutableSharedFlow<Unit>()

    // states
    val searchText
        get() = _searchText.asStateFlow()
    private val _searchText = MutableStateFlow(StringUtils.EMPTY)

    val countryPickerViewState
        get() = _countryPickerViewState.asStateFlow()
    private val _countryPickerViewState = MutableStateFlow(CountryPickerViewState())

    private val defaultCountries = Locale.getAvailableLocales().filterDefaultCountries()

    init {
        launch {
            _searchText.debounce(SEARCH_DELAY)
                .map { searchText -> defaultCountries.filter { it.doesSearchMatch(searchText) } }
                .collect {
                    _countryPickerViewState.value =
                        _countryPickerViewState.value.copy(countries = it)
                }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onLocaleSelected(locale: Locale) {
        launch {
            setLocaleUseCase.execute(locale)
            _closeScreenAction.emit(Unit)
        }
    }

    companion object {
        private const val SEARCH_DELAY = 300L
    }
}
