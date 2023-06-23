package dev.nelson.mot.main.presentations.screen.country_picker

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotCloseIcon
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotNavBackIcon
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.emojiFlag
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
        key1 = Unit,
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
        onCountryClick = { viewModel.onLocaleSelected(it) }
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
) {
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val countriesListScrollState = rememberLazyListState()
    val searchFieldTransitionState = remember { MutableTransitionState(false) }
    val searchFieldTransition = updateTransition(
        transitionState = searchFieldTransitionState,
        label = "searchFieldTransition"
    )

    val searchFieldHorizontalPaddingTransition by searchFieldTransition.animateDp(
        transitionSpec = { tween(durationMillis = Constants.DEFAULT_ANIMATION_DURATION) },
        targetValueByState = { if (it) 0.dp else 24.dp },
        label = "searchFieldHorizontalPaddingTransition"
    )

    val searchFieldVerticalPaddingTransition by searchFieldTransition.animateDp(
        transitionSpec = { tween(durationMillis = Constants.DEFAULT_ANIMATION_DURATION) },
        targetValueByState = { if (it) 0.dp else 12.dp },
        label = "searchFieldVerticalPaddingTransition"
    )

    val searchFieldRoundedCornersTransition by searchFieldTransition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = Constants.DEFAULT_ANIMATION_DURATION,
                easing = FastOutSlowInEasing
            )
        },
        targetValueByState = { if (it) 0.dp else 32.dp },
        label = "searchFieldRoundedCornersTransition"
    )

    Scaffold(
        topBar = {
            MotTopAppBar(
                appBarTitle = "Choose a country",
                navigationIcon = {
                    MotNavBackIcon(
                        onClick = closeScreenAction
                    )
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(appBarScrollBehavior.nestedScrollConnection)
        ) {
            Box(
                modifier = Modifier.padding(
                    horizontal = searchFieldHorizontalPaddingTransition,
                    vertical = searchFieldVerticalPaddingTransition
                )
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusEvent { searchFieldTransitionState.targetState = it.isFocused },
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    singleLine = true,
                    placeholder = { Text(text = "Search") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search icon"
                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            MotCloseIcon(
                                onClick = { onSearchTextChange.invoke(StringUtils.EMPTY) }
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (viewState.countries.isNotEmpty()) {
                            onCountryClick.invoke(viewState.countries.first())
                        }
                    }),
                    shape = RoundedCornerShape(searchFieldRoundedCornersTransition),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    )
                )
            }
            LazyColumn(
                state = countriesListScrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                viewState.countries.forEach { country ->
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

@MotPreviewScreen
@Composable
private fun CountryPickerLayoutPreview() {
    MotMaterialTheme {
        CountryPickerLayout(
            viewState = CountryPickerViewState(),
            onCountryClick = {},
            searchText = "",
            onSearchTextChange = {},
            closeScreenAction = {}
        )
    }
}
