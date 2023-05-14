@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.screen.settings

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
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
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.constant.Constants
import java.util.Locale

@Composable
fun SettingsScreen(
    title: String,
    settingsViewModel: SettingsViewModel,
    navigationIcon: @Composable () -> Unit = {},
) {
    val toastMessage by settingsViewModel.showToastState.collectAsState(StringUtils.EMPTY)
    val viewState by settingsViewModel.settingsViewState.collectAsState()

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
        onDarkThemeSwitchClick = { settingsViewModel.onDarkThemeCheckedChange(it) },
        onDynamicColorThemeSwitchClick = { settingsViewModel.onDynamicColorThemeCheckedChange(it) },
        onExportDataBaseClick = { settingsViewModel.onExportDataBaseClick() },
        onImportDataBaseClick = { filePickerLauncher.launch(Constants.FILE_PICKER_FORMAT) },
        onShowCentsClick = { settingsViewModel.onShowCentsCheckedChange(it) },
        onShowCurrencySymbolClick = { settingsViewModel.onShowCurrencySymbolChange(it) },
    )
}

@Composable
private fun SettingsScreenLayout(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    viewState: SettingsViewState,
    onExportDataBaseClick: () -> Unit,
    onImportDataBaseClick: () -> Unit,
    onDarkThemeSwitchClick: (Boolean) -> Unit,
    onDynamicColorThemeSwitchClick: (Boolean) -> Unit,
    onShowCentsClick: (Boolean) -> Unit,
    onShowCurrencySymbolClick: (Boolean) -> Unit,
) {

    viewState.alertDialog?.let { MotAlertDialog(it) }

    Scaffold(
        topBar = {
            MotTopAppBar(
                appBarTitle = title,
                navigationIcon = navigationIcon
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HeadingListItem(text = "Appearance")
            ListItem(
                headlineContent = { Text(text = "Show cents") },
                supportingContent = {
                    PriceText(
                        locale = viewState.selectedLocale,
                        isShowCents = viewState.isShowCents,
                        isShowCurrencySymbol = viewState.isShowCurrencySymbol,
                        priceInCents = 999999
                    )
                },
                trailingContent = {
                    MotSwitch(
                        checked = viewState.isShowCents,
                        onCheckedChange = onShowCentsClick
                    )
                }
            )
            ListItem(
                headlineContent = { Text(text = "Show currency symbol") },
                supportingContent = {
                    PriceText(
                        locale = viewState.selectedLocale,
                        isShowCents = viewState.isShowCents,
                        isShowCurrencySymbol = viewState.isShowCurrencySymbol,
                        priceInCents = 999999
                    )
                },
                trailingContent = {
                    MotSwitch(
                        checked = viewState.isShowCurrencySymbol,
                        onCheckedChange = onShowCurrencySymbolClick
                    )
                }
            )
            HeadingListItem(text = "Theme")
            ListItem(
                headlineContent = { Text(text = "Dark theme") },
                trailingContent = {
                    MotSwitch(
                        checked = viewState.isDarkThemeSwitchOn,
                        onCheckedChange = onDarkThemeSwitchClick
                    )
                }
            )
            ListItem(
                headlineContent = { Text(text = "Dynamic color theme") },
                trailingContent = {
                    MotSwitch(
                        checked = viewState.isDynamicThemeSwitchOn,
                        onCheckedChange = onDynamicColorThemeSwitchClick
                    )
                }
            )
            HeadingListItem(text = "Data base")
            ListItem(
                headlineContent = { Text(text = "Export data base to the Downloads folder") },
                trailingContent = {
                    MotTextButton(
                        onClick = onExportDataBaseClick,
                        text = "Export"
                    )
                }
            )
            ListItem(
                headlineContent = { Text(text = "Import data base") },
                trailingContent = {
                    MotTextButton(
                        onClick = onImportDataBaseClick,
                        text = "Import"
                    )
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            ListItem(
                headlineContent = { Text(text = "App version: ${BuildConfig.VERSION_NAME}") },
                supportingContent = { Text(text = "Build: ${BuildConfig.VERSION_CODE}") }
            )
        }
    }
}

@Composable
private fun HeadingListItem(text: String) {
    ListItem(
        headlineContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.h6
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
                style = MaterialTheme.typography.body1
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
        viewState = viewState,
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "")
            }
        },
        onExportDataBaseClick = {},
        onDarkThemeSwitchClick = {},
        onImportDataBaseClick = {},
        onDynamicColorThemeSwitchClick = {},
        onShowCentsClick = {},
        onShowCurrencySymbolClick = {}
    )
}
