package dev.nelson.mot.main.presentations.screen.settings.countrypicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.core.ui.widget.AppIconButtons
import dev.nelson.mot.main.util.StringUtils
import dev.utils.preview.MotPreview
import java.util.Locale

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun CountrySearchScreen(
    countryPickerViewState: CountryPickerViewState,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchActiveChange: (Boolean) -> Unit,
    onCountryClick: (Locale) -> Unit
) {
    val searchFieldFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        searchFieldFocusRequester.requestFocus()
    }
    SearchBar(
        modifier = Modifier.focusRequester(searchFieldFocusRequester),
        query = searchText,
        onQueryChange = { onSearchTextChange(it) },
        onSearch = { keyboardController?.hide() },
        active = true,
        leadingIcon = { AppIconButtons.Back { onSearchActiveChange.invoke(false) } },
        placeholder = { Text(text = stringResource(id = android.R.string.search_go)) },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                AppIconButtons.Close { onSearchTextChange.invoke(StringUtils.EMPTY) }
            }
        },
        onActiveChange = { onSearchActiveChange.invoke(it) }
    ) {
        if (countryPickerViewState.countriesSearchResult.isEmpty()) {
            if (searchText.isNotEmpty()) {
                EmptySearchResultPlaceholder()
            }
        } else {
            CountriesList(
                countries = countryPickerViewState.countriesSearchResult,
                onCountryClick = onCountryClick
            )
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

@MotPreview
@Composable
private fun CountrySearchScreenPreview() {
    AppTheme {
        CountrySearchScreen(
            countryPickerViewState = CountryPickerViewState(),
            searchText = "",
            onSearchTextChange = {},
            onSearchActiveChange = {}
        ) {}
    }
}
