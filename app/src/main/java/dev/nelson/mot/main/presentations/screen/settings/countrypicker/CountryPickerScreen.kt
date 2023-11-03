package dev.nelson.mot.main.presentations.screen.settings.countrypicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.core.ui.widget.AppIconButtons
import dev.nelson.mot.core.ui.widget.AppToolbar
import dev.nelson.mot.main.presentations.widgets.AppListPlaceholder
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.extention.emojiFlag
import dev.nelson.mot.main.util.extention.filterDefaultCountries
import dev.utils.preview.MotPreviewScreen
import java.util.Locale

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CountryPickerScreen(
    countryPickerViewModel: CountryPickerViewModel,
    closeScreenAction: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val countryPickerViewState by countryPickerViewModel.countryPickerViewState.collectAsState()
    val searchText by countryPickerViewModel.searchText.collectAsState()

    LaunchedEffect(
        key1 = "close screen action",
        block = {
            countryPickerViewModel.closeScreenAction.collect {
                closeScreenAction.invoke()
            }
        }
    )

    CountryPickerLayout(
        countryPickerViewState = countryPickerViewState,
        searchText = searchText,
        onSearchTextChange = { countryPickerViewModel.onSearchTextChange(it) },
        closeScreenAction = closeScreenAction,
        onCountryClick = {
            keyboardController?.hide()
            countryPickerViewModel.onLocaleSelected(it)
        },
        onSearchActiveChange = { countryPickerViewModel.onSearchActiveChange(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryPickerLayout(
    countryPickerViewState: CountryPickerViewState,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    closeScreenAction: () -> Unit,
    onCountryClick: (Locale) -> Unit,
    onSearchActiveChange: (Boolean) -> Unit
) {
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val countriesListScrollState = rememberLazyListState()

    Box {
        if (countryPickerViewState.isSearchActive) {
            CountrySearchScreen(
                countryPickerViewState,
                searchText,
                onSearchTextChange,
                onSearchActiveChange,
                onCountryClick
            )
        } else {
            LaunchedEffect(
                key1 = "clear search field",
                block = { onSearchTextChange(StringUtils.EMPTY) }
            )

            Scaffold(
                topBar = {
                    AppToolbar.Regular(
                        appBarTitle = stringResource(R.string.choose_a_country_title),
                        navigationIcon = { AppIconButtons.Back(onClick = closeScreenAction) },
                        scrollBehavior = appBarScrollBehavior,
                        actions = { AppIconButtons.Search { onSearchActiveChange.invoke(true) } }
                    )
                }
            ) { innerPadding ->
                CountriesList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .nestedScroll(appBarScrollBehavior.nestedScrollConnection),
                    scrollState = countriesListScrollState,
                    countries = countryPickerViewState.countries,
                    onCountryClick = onCountryClick
                )
            }
        }
    }
}

@Composable
fun CountriesList(
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    countries: List<Locale> = emptyList(),
    onCountryClick: (Locale) -> Unit
) {
    if (countries.isEmpty()) {
        AppListPlaceholder(
            modifier = modifier,
            text = stringResource(R.string.no_countries_found_result)
        )
    } else {
        LazyColumn(
            state = scrollState,
            modifier = modifier
        ) {
            countries.forEach { country ->
                item {
                    ListItem(
                        leadingContent = {
                            Text(
                                text = country.emojiFlag(),
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        },
                        headlineContent = { Text(text = country.displayCountry) },
                        modifier = Modifier.clickable { onCountryClick.invoke(country) }
                    )
                }
            }
        }
    }
}

@MotPreviewScreen
@Composable
private fun DefaultCountryPickerLayoutPreview() {
    AppTheme {
        CountryPickerLayout(
            countryPickerViewState = CountryPickerViewState(),
            searchText = "",
            onSearchTextChange = {},
            closeScreenAction = {},
            onCountryClick = {}
        ) {}
    }
}

@MotPreviewScreen
@Composable
private fun ActiveSearchCountryPickerLayoutPreview() {
    AppTheme {
        CountryPickerLayout(
            countryPickerViewState = CountryPickerViewState(isSearchActive = true),
            searchText = "",
            onSearchTextChange = {},
            closeScreenAction = {},
            onCountryClick = {}
        ) {}
    }
}

@MotPreviewScreen
@Composable
private fun ActiveSearchWithResultsCountryPickerLayoutPreview() {
    AppTheme {
        CountryPickerLayout(
            countryPickerViewState = CountryPickerViewState(
                isSearchActive = true,
                countriesSearchResult = Locale.getAvailableLocales().filterDefaultCountries()
            ),
            searchText = "en",
            onSearchTextChange = {},
            closeScreenAction = {},
            onCountryClick = {}
        ) {}
    }
}

@MotPreviewScreen
@Composable
private fun ActiveSearchNoResultsCountryPickerLayoutPreview() {
    AppTheme {
        CountryPickerLayout(
            countryPickerViewState = CountryPickerViewState(
                isSearchActive = true,
                countriesSearchResult = emptyList()
            ),
            searchText = "eadn",
            onSearchTextChange = {},
            closeScreenAction = {},
            onCountryClick = {}
        ) {}
    }
}
