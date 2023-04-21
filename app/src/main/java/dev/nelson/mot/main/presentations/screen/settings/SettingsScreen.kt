package dev.nelson.mot.main.presentations.screen.settings

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import dev.nelson.mot.main.BuildConfig
import dev.nelson.mot.main.R
import dev.nelson.mot.main.presentations.AlertDialogParams
import dev.nelson.mot.main.presentations.ui.theme.MotTheme
import dev.nelson.mot.main.presentations.ui.theme.colorsMaterial3
import dev.nelson.mot.main.presentations.widgets.MotTopAppBar
import dev.nelson.mot.main.util.StringUtils

@Composable
fun SettingsScreen(
    title: String,
    settingsViewModel: SettingsViewModel,
    navigationIcon: @Composable () -> Unit = {},
) {
    val toastMessage by settingsViewModel.showToastState.collectAsState(StringUtils.EMPTY)
    val darkTheme by settingsViewModel.darkThemeSwitchState.collectAsState(false)
    val colorTheme by settingsViewModel.dynamicColorThemeSwitchState.collectAsState(false)
    val alertDialogState by settingsViewModel.showAlertDialogState.collectAsState(false to null)

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

    if (alertDialogState.first) {
        alertDialogState.second?.let { MotAlertDialog(it) }
    }

    if (toastMessage.isNotEmpty()) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    SettingsScreenLayout(
        title = title,
        navigationIcon = navigationIcon,
        onExportDataBaseClick = { settingsViewModel.onExportDataBaseClick() },
        onImportDataBaseEvent = { settingsViewModel.onImportDataBaseEvent(it) },
        darkTheme = darkTheme,
        colorTheme = colorTheme,
        onDarkClick = { isChecked -> settingsViewModel.onDarkThemeCheckedChange(isChecked) },
    ) { isChecked -> settingsViewModel.onDynamicColorThemeCheckedChange(isChecked) }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenLayout(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    onExportDataBaseClick: () -> Unit,
    onImportDataBaseEvent: (uri: Uri) -> Unit,
    darkTheme: Boolean,
    colorTheme: Boolean,
    onDarkClick: (Boolean) -> Unit,
    onColorClick: (Boolean) -> Unit,
) {

    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onImportDataBaseEvent.invoke(it) }
    }

    Scaffold(
        topBar = { MotTopAppBar(title = title, navigationIcon = navigationIcon) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ListItem(
                trailing = {
                    Switch(
                        checked = darkTheme,
                        onCheckedChange = onDarkClick,
                        colors = SwitchDefaults.colorsMaterial3()
                    )
                },
                text = { Text(text = "Dark theme") }
            )
            ListItem(
                trailing = {
                    Switch(
                        checked = colorTheme,
                        onCheckedChange = onColorClick,
                        colors = SwitchDefaults.colorsMaterial3()
                    )
                },
                text = { Text(text = "Dynamic color theme") }
            )
            ListItem(
                trailing = {
                    TextButton(onClick = { onExportDataBaseClick.invoke() }) { Text(text = "Export") }
                },
                text = { Text(text = "Export data base to the Downloads folder") }
            )
            ListItem(
                trailing = {
                    TextButton(onClick = { filePickerLauncher.launch("*/*") }) { Text(text = "Import") }
                },
                text = { Text(text = "Import data base") }
            )
            Spacer(modifier = Modifier.weight(1f))
            ListItem(
                text = { Text(text = "App version: ${BuildConfig.VERSION_NAME}") },
                secondaryText = { Text(text = "Build: ${BuildConfig.VERSION_CODE}") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenLayoutLightPreview() {
    SettingsScreenLayout(
        title = "Settings",
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "back icon")
            }
        },
        onExportDataBaseClick = {},
        onImportDataBaseEvent = {},
        darkTheme = false,
        colorTheme = true,
        onDarkClick = {}
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenLayoutDarkPreview() {
    MotTheme(darkTheme = true) {
        SettingsScreenLayout(
            title = "Settings",
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "back icon")
                }
            },
            onExportDataBaseClick = {},
            onImportDataBaseEvent = {},
            darkTheme = false,
            colorTheme = true,
            onDarkClick = {}
        ) {}
    }
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
                text = alertDialogParams.message,
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