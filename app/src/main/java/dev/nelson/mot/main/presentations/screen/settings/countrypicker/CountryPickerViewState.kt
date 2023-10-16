package dev.nelson.mot.main.presentations.screen.settings.countrypicker

import dev.nelson.mot.main.util.extention.filterDefaultCountries
import java.util.Locale

data class CountryPickerViewState(
    val countries: List<Locale> = Locale.getAvailableLocales().filterDefaultCountries(),
    val isSearchActive: Boolean = false,
    val countriesSearchResult: List<Locale> = emptyList()
)
