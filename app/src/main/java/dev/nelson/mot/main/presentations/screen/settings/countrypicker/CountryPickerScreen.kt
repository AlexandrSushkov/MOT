package dev.nelson.mot.main.presentations.screen.settings.countrypicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.MotIconButtons
import dev.nelson.mot.core.ui.MotIcons
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotToolbar
import dev.nelson.mot.main.presentations.widgets.EmptyListPlaceholder
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.extention.emojiFlag
import dev.nelson.mot.main.util.extention.filterDefaultCountries
import dev.utils.preview.MotPreviewScreen
import java.util.Locale

@Composable
fun CountryPickerScreen(
    countryPickerViewModel: CountryPickerViewModel,
    closeScreenAction: () -> Unit
) {
    val viewState by countryPickerViewModel.countryPickerViewState.collectAsState()
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
        viewState = viewState,
        searchText = searchText,
        onSearchTextChange = { countryPickerViewModel.onSearchTextChange(it) },
        closeScreenAction = closeScreenAction,
        onCountryClick = { countryPickerViewModel.onLocaleSelected(it) },
        onSearchAction = { countryPickerViewModel.onSearchAction() },
        onSearchActiveChange = { countryPickerViewModel.onSearchActiveChange(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryPickerLayout(
    viewState: CountryPickerViewState,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    closeScreenAction: () -> Unit,
    onCountryClick: (Locale) -> Unit,
    onSearchAction: () -> Unit,
    onSearchActiveChange: (Boolean) -> Unit
) {
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val countriesListScrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            MotToolbar.RegularAppBar(
                appBarTitle = stringResource(R.string.choose_a_country_title),
                navigationIcon = { MotIconButtons.Back(onClick = closeScreenAction) },
                scrollBehavior = appBarScrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                query = searchText,
                onQueryChange = { onSearchTextChange(it) },
                onSearch = { onSearchAction.invoke() },
                active = viewState.isSearchActive,
                leadingIcon = {
                    if (viewState.isSearchActive) {
                        MotIconButtons.Back { onSearchActiveChange.invoke(false) }
                    } else {
                        MotIcons.Search()
                    }
                },
                placeholder = { Text(text = stringResource(id = android.R.string.search_go)) },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        MotIconButtons.Close { onSearchTextChange.invoke(StringUtils.EMPTY) }
                    }
                },
                onActiveChange = { onSearchActiveChange.invoke(it) }
            ) {
                if (viewState.countriesSearchResult.isEmpty()) {
                    if (searchText.isNotEmpty()) {
                        EmptySearchResultPlaceholder()
                    }
                } else {
                    CountriesList(
                        countries = viewState.countriesSearchResult,
                        onCountryClick = onCountryClick
                    )
                }
            }
            if (viewState.countries.isEmpty()) {
                EmptyListPlaceholder(
                    modifier = Modifier.fillMaxSize(),
                    text = stringResource(R.string.no_countries_found_result)
                )
            } else {
                CountriesList(
                    modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection),
                    scrollState = countriesListScrollState,
                    countries = viewState.countries,
                    onCountryClick = onCountryClick
                )
            }
        }
    }
}

@Composable
private fun EmptySearchResultPlaceholder() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(R.string.no_results_found)
        )
    }
}

@Composable
private fun CountriesList(
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    countries: List<Locale> = emptyList(),
    onCountryClick: (Locale) -> Unit
) {
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

@MotPreviewScreen
@Composable
private fun DefaultCountryPickerLayoutPreview() {
    MotMaterialTheme {
        CountryPickerLayout(
            viewState = CountryPickerViewState(),
            onCountryClick = {},
            searchText = "",
            onSearchTextChange = {},
            closeScreenAction = {},
            onSearchAction = {},
            onSearchActiveChange = {}
        )
    }
}

@MotPreviewScreen
@Composable
private fun EmptyResultCountryPickerLayoutPreview() {
    MotMaterialTheme {
        CountryPickerLayout(
            viewState = CountryPickerViewState(
                isSearchActive = false,
                countriesSearchResult = emptyList(),
                countries = emptyList()
            ),
            onCountryClick = {},
            searchText = "enad",
            onSearchTextChange = {},
            closeScreenAction = {},
            onSearchAction = {},
            onSearchActiveChange = {}
        )
    }
}

@MotPreviewScreen
@Composable
private fun ActiveSearchCountryPickerLayoutPreview() {
    MotMaterialTheme {
        CountryPickerLayout(
            viewState = CountryPickerViewState(isSearchActive = true),
            onCountryClick = {},
            searchText = "",
            onSearchTextChange = {},
            closeScreenAction = {},
            onSearchAction = {},
            onSearchActiveChange = {}
        )
    }
}

@MotPreviewScreen
@Composable
private fun ActiveSearchWithResultsCountryPickerLayoutPreview() {
    MotMaterialTheme {
        CountryPickerLayout(
            viewState = CountryPickerViewState(
                isSearchActive = true,
                countriesSearchResult = Locale.getAvailableLocales().filterDefaultCountries()
            ),
            onCountryClick = {},
            searchText = "en",
            onSearchTextChange = {},
            closeScreenAction = {},
            onSearchAction = {},
            onSearchActiveChange = {}
        )
    }
}

@MotPreviewScreen
@Composable
private fun ActiveSearchNoResultsCountryPickerLayoutPreview() {
    MotMaterialTheme {
        CountryPickerLayout(
            viewState = CountryPickerViewState(
                isSearchActive = true,
                countriesSearchResult = emptyList()
            ),
            onCountryClick = {},
            searchText = "eadn",
            onSearchTextChange = {},
            closeScreenAction = {},
            onSearchAction = {},
            onSearchActiveChange = {}
        )
    }
}
