@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.screen.settings

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.nelson.mot.BuildConfig
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotNavDrawerIcon
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
    openAppThemePickerScreen: () -> Unit,
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
        onDynamicColorThemeSwitchChecked = { settingsViewModel.onDynamicColorThemeCheckedChange(it) },
        onExportDataBaseClick = { settingsViewModel.onExportDataBaseClick() },
        onImportDataBaseClick = { filePickerLauncher.launch(Constants.FILE_PICKER_FORMAT) },
        onShowCentsSwitchChecked = { settingsViewModel.onShowCentsCheckedChange(it) },
        onShowCurrencySymbolSwitchChecked = { settingsViewModel.onShowCurrencySymbolChange(it) },
        onHideDigitsSwitchChecked = { settingsViewModel.onHideDigitsChange(it) },
        onRandomizeBaseDataSwitchChecked = { settingsViewModel.onRandomizeDataBaseDataClick() },
        onAppThemeClicked = openAppThemePickerScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenLayout(
    title: String,
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
    onHideDigitsSwitchChecked: (Boolean) -> Unit,
) {
    viewState.alertDialog?.let { MotAlertDialog(it) }

    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val settingsScreenContentScrollingState = rememberLazyListState()

    Scaffold(
        topBar = {
            MotTopAppBar(
                appBarTitle = title,
                navigationIcon = navigationIcon,
                scrollBehavior = appBarScrollBehavior,
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
            item { HeadingListøItem(text = "Appearance") }
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
                            onCheckedChange = onShowCentsSwitchChecked
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
                            onCheckedChange = onShowCurrencySymbolSwitchChecked
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
                            onCheckedChange = onHideDigitsSwitchChecked,
                            uncheckedStateIcon = Icons.Default.Visibility,
                            checkedStateIcon = Icons.Default.VisibilityOff
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
            item {
                ListItem(
                    modifier = Modifier.clickable { onAppThemeClicked.invoke() },
                    headlineContent = { Text(text = "App Theme") },
                    trailingContent = {
                        Text(
                            text = viewState.selectedAppTheme.name,
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(text = "Dynamic Color Theme") },
                    trailingContent = {
                        MotSwitch(
                            checked = viewState.isDynamicThemeSwitchChecked,
                            onCheckedChange = onDynamicColorThemeSwitchChecked
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
                                onClick = onRandomizeBaseDataSwitchChecked,
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
                contentDescription = stringResource(R.string.accessibility_info_icon),
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
            isShowCentsSwitchChecked = true,
            isShowCurrencySymbolSwitchChecked = true,
//            alertDialog = alertDialogParams,
        )
        SettingsScreenLayout(
            title = "Settings",
            navigationIcon = {
                MotNavDrawerIcon {

                }
            },
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
