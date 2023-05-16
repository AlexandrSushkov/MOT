@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.screen.settings

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotSwitch
import dev.nelson.mot.core.ui.MotTextButton
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.nelson.mot.core.ui.PriceText
import dev.nelson.mot.main.BuildConfig
import dev.nelson.mot.main.R
import dev.nelson.mot.main.presentations.AlertDialogParams
import dev.nelson.mot.main.presentations.widgets.MotModalBottomSheetLayout
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.emojiFlag
import kotlinx.coroutines.launch
import java.util.Locale
import androidx.compose.material3.MaterialTheme as MaterialTheme

@Composable
fun SettingsScreen(
    title: String,
    settingsViewModel: SettingsViewModel,
    navigationIcon: @Composable () -> Unit = {},
) {
    val toastMessage by settingsViewModel.showToastState.collectAsState(StringUtils.EMPTY)
    val viewState by settingsViewModel.settingsViewState.collectAsState()
    val countries by settingsViewModel.localesState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(
        key1 = Unit,
        block = {
            settingsViewModel.restartAppAction.collect {
                context.packageManager
                    .getLaunchIntentForPackage(context.packageName)
                    ?.component
                    .let { Intent.makeRestartActivityTask(it) }
                    .let {
                        context.startActivity(it)
                        Runtime.getRuntime().exit(0)
                    }
            }
        }
    )

    if (toastMessage.isNotEmpty()) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    val filePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { settingsViewModel.onImportDataBaseEvent(it) }
        }

    SettingsScreenLayout(
        title = title,
        navigationIcon = navigationIcon,
        viewState = viewState,
        countries = countries,
        onCountryClick = { settingsViewModel.onLocaleSelected(it) },
        onDarkThemeSwitchClick = { settingsViewModel.onDarkThemeCheckedChange(it) },
        onDynamicColorThemeSwitchClick = { settingsViewModel.onDynamicColorThemeCheckedChange(it) },
        onExportDataBaseClick = { settingsViewModel.onExportDataBaseClick() },
        onImportDataBaseClick = { filePickerLauncher.launch(Constants.FILE_PICKER_FORMAT) },
        onShowCentsClick = { settingsViewModel.onShowCentsCheckedChange(it) },
        onShowCurrencySymbolClick = { settingsViewModel.onShowCurrencySymbolChange(it) },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SettingsScreenLayout(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    viewState: SettingsViewState,
    countries: List<Locale>,
    onCountryClick: (Locale) -> Unit,
    onExportDataBaseClick: () -> Unit,
    onImportDataBaseClick: () -> Unit,
    onDarkThemeSwitchClick: (Boolean) -> Unit,
    onDynamicColorThemeSwitchClick: (Boolean) -> Unit,
    onShowCentsClick: (Boolean) -> Unit,
    onShowCurrencySymbolClick: (Boolean) -> Unit,
) {

    viewState.alertDialog?.let { MotAlertDialog(it) }
    val modalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
    val scope = rememberCoroutineScope()

    MotModalBottomSheetLayout(
        sheetContent = {
            CountiesListBottomSheet(
                countries = countries,
                onCountryClick = onCountryClick,
                modalBottomSheetState
            )
        },
        sheetState = modalBottomSheetState,
    ) {
        Scaffold(
            topBar = {
                MotTopAppBar(
                    appBarTitle = title,
                    navigationIcon = navigationIcon
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                item { HeadingListItem(text = "Appearance") }
                item {
                    ListItem(
                        headlineContent = { Text(text = "Show Cents") },
                        supportingContent = {
                            PriceText(
                                locale = viewState.selectedLocale,
                                isShowCents = viewState.isShowCents,
                                isShowCurrencySymbol = viewState.isShowCurrencySymbol,
                                priceInCents = Constants.PRICE_EXAMPLE
                            )
                        },
                        trailingContent = {
                            MotSwitch(
                                checked = viewState.isShowCents,
                                onCheckedChange = onShowCentsClick
                            )
                        }
                    )
                }
                item {
                    ListItem(
                        headlineContent = { Text(text = "Show Currency Symbol") },
                        supportingContent = {
                            PriceText(
                                locale = viewState.selectedLocale,
                                isShowCents = viewState.isShowCents,
                                isShowCurrencySymbol = viewState.isShowCurrencySymbol,
                                priceInCents = Constants.PRICE_EXAMPLE
                            )
                        },
                        trailingContent = {
                            MotSwitch(
                                checked = viewState.isShowCurrencySymbol,
                                onCheckedChange = onShowCurrencySymbolClick
                            )
                        }
                    )
                }
                item {
                    ListItem(
                        modifier = Modifier.clickable { scope.launch { modalBottomSheetState.show()} },
                        headlineContent = { Text(text = "Locale") },
                        supportingContent = { Text(text = viewState.selectedLocale.displayCountry) },
                        trailingContent = {
                            Text(
                                text = viewState.selectedLocale.emojiFlag(),
                                modifier = Modifier.padding(end = 8.dp),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        },
                    )
                }
                item {
                    HeadingListItem(text = "Theme")
                }
                item {
                    ListItem(
                        headlineContent = { Text(text = "Force Dark Theme") },
                        trailingContent = {
                            MotSwitch(
                                checked = viewState.isDarkThemeSwitchOn,
                                onCheckedChange = onDarkThemeSwitchClick
                            )
                        }
                    )
                }

                item {
                    ListItem(
                        headlineContent = { Text(text = "Dynamic Color Theme") },
                        trailingContent = {
                            MotSwitch(
                                checked = viewState.isDynamicThemeSwitchOn,
                                onCheckedChange = onDynamicColorThemeSwitchClick
                            )
                        }
                    )
                }
                item {
                    HeadingListItem(text = "Data base")
                }
                item {
                    ListItem(
                        headlineContent = { Text(text = "Export Data Base") },
                        supportingContent = { Text(text = "It will be exported to the Downloads folder.") },
                        trailingContent = {
                            MotTextButton(
                                onClick = onExportDataBaseClick,
                                text = "Export"
                            )
                        }
                    )
                }
                item {
                    ListItem(
                        headlineContent = { Text(text = "Import Data Base") },
                        trailingContent = {
                            MotTextButton(
                                onClick = onImportDataBaseClick,
                                text = "Import"
                            )
                        }
                    )
                }
                item {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = "App version: ${BuildConfig.VERSION_NAME}",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        supportingContent = {
                            Text(
                                text = "Build: ${BuildConfig.VERSION_CODE}",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HeadingListItem(text: String) {
    ListItem(
        headlineContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        },
    )
}

@Composable
fun MotAlertDialog(alertDialogParams: AlertDialogParams) {
    AlertDialog(
        onDismissRequest = alertDialogParams.dismissClickCallback,
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = stringResource(R.string.info_icon),
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp)
            )
        },
        text = {
            Text(
                text = stringResource(alertDialogParams.message),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        dismissButton = {
            alertDialogParams.onNegativeClickCallback?.let {
                TextButton(onClick = it) {
                    Text(text = stringResource(android.R.string.cancel))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = alertDialogParams.onPositiveClickCallback) {
                Text(text = stringResource(android.R.string.ok))
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CountiesListBottomSheet(
    countries: List<Locale>,
    onCountryClick: (Locale) -> Unit,
    modalBottomSheetState: ModalBottomSheetState
) {
    val scope = rememberCoroutineScope()
    val layColumnState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Choose a country",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }
        LazyColumn(
            state = layColumnState,
            modifier = Modifier
                .fillMaxSize()
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
                        modifier = Modifier
                            .clickable {
                                onCountryClick.invoke(country)
                                scope.launch {
                                    modalBottomSheetState.hide()
                                    layColumnState.scrollToItem(0)
                                }
                            }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun SettingsScreenLayoutLightPreview() {
    MotMaterialTheme(darkTheme = false) {
        SettingsScreenLayoutPreviewData()
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenLayoutDarkPreview() {
    MotMaterialTheme(darkTheme = true) {
        SettingsScreenLayoutPreviewData()
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenLayoutDynamicPreview() {
    MotMaterialTheme(dynamicColor = true) {
        SettingsScreenLayoutPreviewData()
    }
}

@Composable
private fun SettingsScreenLayoutPreviewData() {
    val alertDialogParams = AlertDialogParams(
        message = R.string.database_export_failed_dialog_message,
        onPositiveClickCallback = {},
//        onNegativeClickCallback = {},
        dismissClickCallback = {}
    )
    val viewState = SettingsViewState(
        isDarkThemeSwitchOn = false,
        isDynamicThemeSwitchOn = true,
        isShowCents = false
//        alertDialog = alertDialogParams,
    )
    SettingsScreenLayout(
        title = "Settings",
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "")
            }
        },
        viewState = viewState,
        countries = emptyList(),
        onExportDataBaseClick = {},
        onImportDataBaseClick = {},
        onDarkThemeSwitchClick = {},
        onDynamicColorThemeSwitchClick = {},
        onShowCentsClick = {},
        onShowCurrencySymbolClick = {},
        onCountryClick = {}
    )
}
