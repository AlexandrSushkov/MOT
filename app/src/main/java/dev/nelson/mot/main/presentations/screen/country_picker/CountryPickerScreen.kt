package dev.nelson.mot.main.presentations.screen.country_picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotCloseIcon
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotNavBackIcon
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.nelson.mot.main.util.extention.emojiFlag
import dev.nelson.mot.main.util.extention.filterDefaultCountries
import java.util.Locale

@Composable
fun CountryPickerScreen(
    viewModel: CountryPickerViewModel,
    closeScreen: () -> Unit
) {
    val countries by viewModel.countriesPickerState.collectAsState(emptyList())
    val searchText by viewModel.searchText.collectAsState()

    LaunchedEffect(
        key1 = Unit,
        block = {
            viewModel.closeScreenAction.collect {
                closeScreen.invoke()
            }
        }
    )

    CountryPickerLayout(countries,
        searchText,
        viewModel::onSearchTextChange,
        closeScreen,
        onCountryClick = { viewModel.onLocaleSelected(it) }
    )
}

@Composable
private fun CountryPickerLayout(
    countries: List<Locale>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    closeScreen: () -> Unit,
    onCountryClick: (Locale) -> Unit,
) {
    Scaffold(
        topBar = {
            MotTopAppBar(
                appBarTitle = "Choose a country",
                navigationIcon = {
                    MotNavBackIcon(
                        onClick = closeScreen
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchText,
                onValueChange = onSearchTextChange,
                singleLine = true,
                placeholder = { Text(text = "Search") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "") },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        MotCloseIcon(
                            onClick = { onSearchTextChange.invoke("") }
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (countries.isNotEmpty()) {
                        onCountryClick.invoke(countries.first())
                    }
                })

            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
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
}

@Preview(showBackground = false)
@Composable
private fun CountryPickerLayout() {
    MotMaterialTheme(darkTheme = false) {
        CountryPickerLayout(
            countries = Locale.getAvailableLocales().filterDefaultCountries(),
            onCountryClick = {},
            searchText = "",
            onSearchTextChange = {},
            closeScreen = {}
        )
    }
}
