package dev.nelson.mot.main.presentations.screen.settings.countrypicker

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.domain.usecase.settings.SetLocaleUseCase
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CountryPickerViewModel @Inject constructor(
    private val setLocaleUseCase: SetLocaleUseCase
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

        /**
         * this is for search result
         */
        launch {
            _searchText.map { searchText ->
                if (searchText.isEmpty()) {
                    emptyList()
                } else {
                    defaultCountries.filter { it.doesSearchMatch(searchText) }
                }
            }
                .collect { newLocaleList ->
                    _countryPickerViewState.update {
                        it.copy(
                            countriesSearchResult = newLocaleList
                        )
                    }
                }
        }

        /**
         * this is for list of countries
         */
        launch {
            _searchText.debounce(SEARCH_DELAY)
                .map { searchText -> defaultCountries.filter { it.doesSearchMatch(searchText) } }
                .collect { newLocaleList ->
                    _countryPickerViewState.update {
                        it.copy(
                            countries = newLocaleList
                        )
                    }
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

    fun onSearchAction() {
        _countryPickerViewState.update { it.copy(isSearchActive = false) }
    }

    fun onSearchActiveChange(isActive: Boolean) {
        _countryPickerViewState.update { it.copy(isSearchActive = isActive) }
    }

    companion object {
        private const val SEARCH_DELAY = 300L
    }
}
