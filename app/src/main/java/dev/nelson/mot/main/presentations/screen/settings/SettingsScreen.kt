@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.screen.settings

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.nelson.mot.BuildConfig
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotSwitch
import dev.nelson.mot.core.ui.MotTextButton
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.nelson.mot.core.ui.PriceText
import dev.nelson.mot.main.presentations.AlertDialogParams
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.emojiFlag
import dev.utils.preview.MotPreviewScreen

@Composable
fun SettingsScreen(
    title: String,
    settingsViewModel: SettingsViewModel,
    navigationIcon: @Composable () -> Unit = {},
    openCountryPickerScreen: () -> Unit,
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
        title = title,
        navigationIcon = navigationIcon,
        viewState = viewState,
        onLocaleClick = openCountryPickerScreen,
        onDarkThemeSwitchClick = { settingsViewModel.onForceDarkThemeCheckedChange(it) },
        onDynamicColorThemeSwitchClick = { settingsViewModel.onDynamicColorThemeCheckedChange(it) },
        onExportDataBaseClick = { settingsViewModel.onExportDataBaseClick() },
        onImportDataBaseClick = { filePickerLauncher.launch(Constants.FILE_PICKER_FORMAT) },
        onShowCentsClick = { settingsViewModel.onShowCentsCheckedChange(it) },
        onShowCurrencySymbolClick = { settingsViewModel.onShowCurrencySymbolChange(it) },
        onHideDigitsClick = { settingsViewModel.onHideDigitsChange(it) },
        onRandomizeBaseDataClick = { settingsViewModel.onRandomizeDataBaseDataClick() },
    )
}

@Composable
private fun SettingsScreenLayout(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    viewState: SettingsViewState,
    onLocaleClick: () -> Unit,
    onExportDataBaseClick: () -> Unit,
    onImportDataBaseClick: () -> Unit,
    onRandomizeBaseDataClick: () -> Unit,
    onDarkThemeSwitchClick: (Boolean) -> Unit,
    onDynamicColorThemeSwitchClick: (Boolean) -> Unit,
    onShowCentsClick: (Boolean) -> Unit,
    onShowCurrencySymbolClick: (Boolean) -> Unit,
    onHideDigitsClick: (Boolean) -> Unit,
) {

    viewState.alertDialog?.let { MotAlertDialog(it) }
    val settingsScreenContentScrollingState = rememberLazyListState()
    val isSystemInLightTheme = isSystemInDarkTheme().not()

    Scaffold(
        topBar = {
            MotTopAppBar(
                appBarTitle = title,
                navigationIcon = navigationIcon,
                isScrolling = settingsScreenContentScrollingState.firstVisibleItemIndex != 0
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = settingsScreenContentScrollingState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item { HeadingListItem(text = "Appearance") }
            item {
                ListItem(
                    headlineContent = { Text(text = "Price Field Example") },
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
                    headlineContent = { Text(text = "Show Cents") },
                    trailingContent = {
                        MotSwitch(
                            checked = viewState.isShowCentsSwitchChecked,
                            onCheckedChange = onShowCentsClick
                        )
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(text = "Show Currency Symbol") },
                    trailingContent = {
                        MotSwitch(
                            checked = viewState.isShowCurrencySymbolSwitchChecked,
                            onCheckedChange = onShowCurrencySymbolClick
                        )
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(text = "Hide digits") },
                    trailingContent = {
                        MotSwitch(
                            checked = viewState.isHideDigitsSwitchChecked,
                            onCheckedChange = onHideDigitsClick
                        )
                    }
                )
            }
            item {
                ListItem(
                    modifier = Modifier.clickable { onLocaleClick.invoke() },
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
            if(isSystemInLightTheme){
                item {
                    ListItem(
                        headlineContent = { Text(text = "Force Dark Theme") },
                        trailingContent = {
                            MotSwitch(
                                checked = viewState.isForceDarkThemeSwitchChecked,
                                onCheckedChange = onDarkThemeSwitchClick
                            )
                        }
                    )
                }
            }
            item {
                ListItem(
                    headlineContent = { Text(text = "Dynamic Color Theme") },
                    trailingContent = {
                        MotSwitch(
                            checked = viewState.isDynamicThemeSwitchChecked,
                            onCheckedChange = onDynamicColorThemeSwitchClick
                        )
                    }
                )
            }
            item {
                HeadingListItem(text = "Database")
            }
            item {
                ListItem(
                    headlineContent = { Text(text = "Export Database") },
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
                    headlineContent = { Text(text = "Import Database") },
                    trailingContent = {
                        MotTextButton(
                            onClick = onImportDataBaseClick,
                            text = "Import"
                        )
                    }
                )
            }
            if (BuildConfig.DEBUG) {
                item {
                    ListItem(
                        headlineContent = { Text(text = "Randomize Database data") },
                        trailingContent = {
                            MotTextButton(
                                onClick = onRandomizeBaseDataClick,
                                text = "Randomize"
                            )
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

@MotPreviewScreen
@Composable
private fun SettingsScreenLayoutPreview() {
    MotMaterialTheme {
        val alertDialogParams = AlertDialogParams(
            message = R.string.database_export_failed_dialog_message,
            onPositiveClickCallback = {},
//            onNegativeClickCallback = {},
            dismissClickCallback = {}
        )
        val viewState = SettingsViewState(
            isForceDarkThemeSwitchChecked = false,
            isDynamicThemeSwitchChecked = true,
            isShowCentsSwitchChecked = true,
            isShowCurrencySymbolSwitchChecked = true,
//            alertDialog = alertDialogParams,
        )
        SettingsScreenLayout(
            title = "Settings",
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "")
                }
            },
            viewState = viewState,
            onLocaleClick = {},
            onExportDataBaseClick = {},
            onImportDataBaseClick = {},
            onDarkThemeSwitchClick = {},
            onDynamicColorThemeSwitchClick = {},
            onShowCentsClick = {},
            onShowCurrencySymbolClick = {},
            onHideDigitsClick = {},
            onRandomizeBaseDataClick = {},
        )
    }
}
