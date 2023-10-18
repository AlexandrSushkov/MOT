@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.screen.settings

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltipBox
import androidx.compose.material3.RichTooltipState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.nelson.mot.BuildConfig
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.AppButtons
import dev.nelson.mot.core.ui.AppIconButtons
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.core.ui.MotSwitch
import dev.nelson.mot.core.ui.AppToolbar
import dev.nelson.mot.core.ui.PriceText
import dev.nelson.mot.main.presentations.widgets.MotAlertDialog
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.emojiFlag
import dev.utils.preview.MotPreviewScreen
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    navigationIcon: @Composable () -> Unit = {},
    openCountryPickerScreen: () -> Unit,
    openAppThemePickerScreen: () -> Unit
) {
    val context = LocalContext.current

    val toastMessage by settingsViewModel.showToastState.collectAsState(StringUtils.EMPTY)
    val viewState by settingsViewModel.settingsViewState.collectAsState()

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
        navigationIcon = navigationIcon,
        viewState = viewState,
        onLocaleClick = openCountryPickerScreen,
        onDynamicColorThemeSwitchChecked = { settingsViewModel.onDynamicColorThemeCheckedChange(it) },
        onExportDataBaseClick = { settingsViewModel.onExportDataBaseClick() },
        onImportDataBaseClick = { filePickerLauncher.launch(Constants.FILE_PICKER_FORMAT) },
        onShowCentsSwitchChecked = { settingsViewModel.onShowCentsCheckedChange(it) },
        onShowCurrencySymbolSwitchChecked = { settingsViewModel.onShowCurrencySymbolChange(it) },
        onHideDigitsSwitchChecked = { settingsViewModel.onHideDigitsChange(it) },
        onRandomizeBaseDataSwitchChecked = { settingsViewModel.onRandomizeDataBaseDataClick() },
        onAppThemeClicked = openAppThemePickerScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenLayout(
    navigationIcon: @Composable () -> Unit = {},
    viewState: SettingsViewState,
    onLocaleClick: () -> Unit,
    onExportDataBaseClick: () -> Unit,
    onImportDataBaseClick: () -> Unit,
    onRandomizeBaseDataSwitchChecked: () -> Unit,
    onAppThemeClicked: () -> Unit,
    onDynamicColorThemeSwitchChecked: (Boolean) -> Unit,
    onShowCentsSwitchChecked: (Boolean) -> Unit,
    onShowCurrencySymbolSwitchChecked: (Boolean) -> Unit,
    onHideDigitsSwitchChecked: (Boolean) -> Unit
) {
    viewState.alertDialog?.let { MotAlertDialog(it) }

    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val settingsScreenContentScrollingState = rememberLazyListState()

    Scaffold(
        topBar = {
            AppToolbar.Regular(
                appBarTitle = stringResource(id = R.string.settings),
                navigationIcon = navigationIcon,
                scrollBehavior = appBarScrollBehavior
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = settingsScreenContentScrollingState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .nestedScroll(appBarScrollBehavior.nestedScrollConnection)
        ) {
            item { HeadingListItem(text = stringResource(R.string.appearance_text)) }
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.price_field_text)) },
                    trailingContent = {
                        PriceText(
                            price = Constants.PRICE_EXAMPLE,
                            priceViewState = viewState.priceViewState
                        )
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.show_cents_text)) },
                    trailingContent = {
                        MotSwitch(
                            checked = viewState.isShowCentsSwitchChecked,
                            onCheckedChange = onShowCentsSwitchChecked
                        )
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.show_currency_symbol_text)) },
                    trailingContent = {
                        MotSwitch(
                            checked = viewState.isShowCurrencySymbolSwitchChecked,
                            onCheckedChange = onShowCurrencySymbolSwitchChecked
                        )
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.show_digits_text)) },
                    trailingContent = {
                        MotSwitch(
                            checked = viewState.isShowDigitsSwitchChecked,
                            onCheckedChange = onHideDigitsSwitchChecked,
                            uncheckedStateIcon = Icons.Default.VisibilityOff,
                            checkedStateIcon = Icons.Default.Visibility
                        )
                    }
                )
            }
            item {
                ListItem(
                    modifier = Modifier.clickable { onLocaleClick.invoke() },
                    headlineContent = { Text(text = stringResource(R.string.locale_text)) },
                    supportingContent = { Text(text = viewState.selectedLocale.displayCountry) },
                    trailingContent = {
                        Text(
                            text = viewState.selectedLocale.emojiFlag(),
                            modifier = Modifier.padding(end = 8.dp),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                )
            }
            item {
                Divider()
            }
            item {
                HeadingListItem(text = stringResource(R.string.theme_text))
            }
            item {
                ListItem(
                    modifier = Modifier.clickable { onAppThemeClicked.invoke() },
                    headlineContent = { Text(text = stringResource(R.string.app_theme_text)) },
                    trailingContent = {
                        Text(
                            text = viewState.selectedAppTheme.javaClass.name,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.dynamic_color_theme_text)) },
                    trailingContent = {
                        MotSwitch(
                            checked = viewState.isDynamicThemeSwitchChecked,
                            onCheckedChange = onDynamicColorThemeSwitchChecked
                        )
                    }
                )
            }
            item {
                Divider()
            }
            item {
                HeadingListItem(text = stringResource(R.string.database_text))
            }
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.export_database_text)) },
                    supportingContent = { Text(text = stringResource(R.string.export_data_base_message)) },
                    trailingContent = {
                        AppButtons.TextButton(
                            text = stringResource(R.string.export_text),
                            onClick = onExportDataBaseClick
                        )
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(R.string.import_database_text)) },
                    trailingContent = {
                        AppButtons.TextButton(
                            text = stringResource(R.string.import_text),
                            onClick = onImportDataBaseClick
                        )
                    }
                )
            }
            if (BuildConfig.DEBUG) {
                item {
                    ListItem(
                        headlineContent = { Text(text = "Randomize Database data") },
                        trailingContent = {
                            AppButtons.TextButton(
                                text = "Randomize",
                                onClick = onRandomizeBaseDataSwitchChecked
                            )
                        }
                    )
                }

                item {
                    val tooltipState = remember { RichTooltipState() }
                    val scope = rememberCoroutineScope()
                    ListItem(
                        headlineContent = { Text(text = "RichTooltip") },
                        trailingContent = {
                            RichTooltipBox(
                                title = { Text(text = "RichTooltip title") },
                                text = { Text(text = "RichTooltip text") },
                                action = {
                                    TextButton(
                                        onClick = { scope.launch { tooltipState.dismiss() } }
                                    ) { Text("Learn More") }
                                },
                                tooltipState = tooltipState

                            ) {
                                IconButton(
                                    onClick = { /* Icon button's click event */ },
                                    modifier = Modifier.tooltipAnchor()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "info icon"
                                    )
                                }
                            }
                        }
                    )
                }
            }
            item { Spacer(Modifier.height(60.dp)) }
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = "App version: ${BuildConfig.VERSION_NAME}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    supportingContent = {
                        Text(
                            text = "Build: ${BuildConfig.VERSION_CODE}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
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
        }
    )
}

@MotPreviewScreen
@Composable
private fun SettingsScreenLayoutPreview() {
    val viewState = SettingsViewState(
        isShowCentsSwitchChecked = true,
        isShowCurrencySymbolSwitchChecked = true
    )
    AppTheme {
        SettingsScreenLayout(
            navigationIcon = { AppIconButtons.Drawer {} },
            viewState = viewState,
            onLocaleClick = {},
            onExportDataBaseClick = {},
            onImportDataBaseClick = {},
            onDynamicColorThemeSwitchChecked = {},
            onShowCentsSwitchChecked = {},
            onShowCurrencySymbolSwitchChecked = {},
            onHideDigitsSwitchChecked = {},
            onRandomizeBaseDataSwitchChecked = {},
            onAppThemeClicked = {}
        )
    }
}
