package dev.nelson.mot.main.presentations.screen.country_picker

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.domain.use_case.settings.SetLocaleUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.extention.doesSearchMatch
import dev.nelson.mot.main.util.extention.filterDefaultCountries
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
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
//    private val _countriesPickerState =
//        MutableStateFlow(Locale.getAvailableLocales().filterDefaultCountries())

    val searchText
        get() = _searchText.asStateFlow()
    private val _searchText = MutableStateFlow(StringUtils.EMPTY)

    private val _onScrollChanged = MutableStateFlow(0)

    val viewState
        get() = _viewState.asStateFlow()
    private val _viewState = MutableStateFlow(CountryPickerViewState())

    init {
        _searchText.debounce(300)
            .combine(_viewState) { searchText, viewState ->
                viewState.countries.filter { it.doesSearchMatch(searchText) }
            }.stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                _viewState.value
//                _countriesPickerState.value
            )

        launch {
            _onScrollChanged.combine(_viewState) { pos1, vs ->
                pos1 != vs.firstVisibleItemIndex
                return@combine pos1
            }.collect {
                if (it == 0 && _viewState.value.firstVisibleItemIndex != 0) {
                    // scrolled to top. first item is visible. change the color of the toolbar
                    _viewState.value = _viewState.value.copy(
                        firstVisibleItemIndex = it,
                        isContentScrolling = false
                    )
                    return@collect
                }
                if (it != 0 && _viewState.value.firstVisibleItemIndex == 0) {
                    // scroll is started. first item is NOT visible. change the color of the toolbar
                    _viewState.value = _viewState.value.copy(
                        firstVisibleItemIndex = it,
                        isContentScrolling = true
                    )
                    return@collect
                }
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onCountryListScrolledChanged(firstVisibleItemPosition: Int) {
        Timber.d("onCountryListScrolledChanged: $firstVisibleItemPosition")

        _onScrollChanged.value = firstVisibleItemPosition
        Timber.d("_onScrollChanged: ${_onScrollChanged.value}")
//        _searchText.value = text
//        if (firstVisibleItemPosition == 0 && _viewState.value.firstVisibleItemIndex != 0) {
//            _viewState.value = _viewState.value.copy(
//                firstVisibleItemIndex = firstVisibleItemPosition,
//                isContentScrolling = false
//            )
//            return
//        }
//        if (firstVisibleItemPosition != 0 && _viewState.value.firstVisibleItemIndex == 0) {
//            _viewState.value = _viewState.value.copy(
//                firstVisibleItemIndex = firstVisibleItemPosition,
//                isContentScrolling = true
//            )
//            return
//        }
    }

    fun onLocaleSelected(locale: Locale) {
        launch {
            setLocaleUseCase.execute(locale)
            _closeScreenAction.emit(Unit)
        }
    }
}
