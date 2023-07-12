package dev.nelson.mot.main.presentations.screen.settings.country_picker

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotCloseIcon
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotNavBackIcon
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.nelson.mot.main.presentations.widgets.EmptyListPlaceholder
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.extention.emojiFlag
import dev.nelson.mot.main.util.extention.filterDefaultCountries
import dev.utils.preview.MotPreviewScreen
import java.util.Locale

@Composable
fun CountryPickerScreen(
    viewModel: CountryPickerViewModel,
    closeScreenAction: () -> Unit
) {
    val viewState by viewModel.countryPickerViewState.collectAsState()
    val searchText by viewModel.searchText.collectAsState()

    LaunchedEffect(
        key1 = "close screen action",
        block = {
            viewModel.closeScreenAction.collect {
                closeScreenAction.invoke()
            }
        }
    )

    CountryPickerLayout(
        viewState = viewState,
        searchText = searchText,
        onSearchTextChange = { viewModel.onSearchTextChange(it) },
        closeScreenAction = closeScreenAction,
        onCountryClick = { viewModel.onLocaleSelected(it) },
        onSearchAction = { viewModel.onSearchAction() },
        onSearchActiveChange = { viewModel.onSearchActiveChange(it) }
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
            MotTopAppBar(
                appBarTitle = "Choose a country",
                navigationIcon = {
                    MotNavBackIcon(
                        onClick = closeScreenAction
                    )
                },
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
                        MotNavBackIcon {
                            onSearchActiveChange.invoke(false)
                        }
                    } else {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search icon"
                        )
                    }
                },
                placeholder = { Text(text = "Search") },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        MotCloseIcon(
                            onClick = { onSearchTextChange.invoke(StringUtils.EMPTY) }
                        )
                    }
                },
                onActiveChange = { onSearchActiveChange.invoke(it) }) {
                if (viewState.countriesSearchResult.isEmpty()) {
                    if (searchText.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 32.dp),
                                text = "No results found."
                            )
                        }
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
                    text = "No countries found."
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
private fun CountriesList(
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    countries: List<Locale> = emptyList(),
    onCountryClick: (Locale) -> Unit,
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
private fun CountryPickerDefaultLayoutPreview() {
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
private fun CountryPickerEmptyResultLayoutPreview() {
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
private fun CountryPickerActiveSearchLayoutPreview() {
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
private fun CountryPickerActiveSearchWithResultsLayoutPreview() {
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
private fun CountryPickerActiveSearchNoResultsLayoutPreview() {
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
