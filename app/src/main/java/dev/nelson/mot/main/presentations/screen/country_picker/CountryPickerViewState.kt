package dev.nelson.mot.main.presentations.screen.country_picker

import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.extention.filterDefaultCountries
import java.util.Locale

data class CountryPickerViewState(
    val countries: List<Locale> = Locale.getAvailableLocales().filterDefaultCountries(),
    val searchText: String = StringUtils.EMPTY,
    val firstVisibleItemIndex: Int = 0,
    val isContentScrolling: Boolean = false
)
