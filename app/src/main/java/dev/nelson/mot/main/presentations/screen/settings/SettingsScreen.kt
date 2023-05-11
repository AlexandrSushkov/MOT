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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
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
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.BuildConfig
import dev.nelson.mot.main.R
import dev.nelson.mot.main.presentations.AlertDialogParams
import dev.nelson.mot.core.ui.MotSwitch
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.nelson.mot.main.util.StringUtils

private const val FILE_PICKER_FORMAT = "*/*"

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

    val filePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { settingsViewModel.onImportDataBaseEvent(it) }
        }

    SettingsScreenLayout(
        title = title,
        navigationIcon = navigationIcon,
        onExportDataBaseClick = { settingsViewModel.onExportDataBaseClick() },
        darkTheme = darkTheme,
        colorTheme = colorTheme,
        onImportDataBaseClick = { filePickerLauncher.launch(FILE_PICKER_FORMAT) },
        onDarkClick = { isChecked -> settingsViewModel.onDarkThemeCheckedChange(isChecked) },
    ) { isChecked -> settingsViewModel.onDynamicColorThemeCheckedChange(isChecked) }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenLayout(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    onExportDataBaseClick: () -> Unit,
    darkTheme: Boolean,
    colorTheme: Boolean,
    onImportDataBaseClick: () -> Unit,
    onDarkClick: (Boolean) -> Unit,
    onColorClick: (Boolean) -> Unit,
) {

    Scaffold(
        topBar = { MotTopAppBar(title = title, navigationIcon = navigationIcon) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ListItem(
                text = { Text(text = "Dark theme") },
                trailing = {
                    MotSwitch(
                        checked = darkTheme,
                        onCheckedChange = onDarkClick
                    )
                }
            )
            ListItem(
                text = { Text(text = "Dynamic color theme") },
                trailing = {
                    MotSwitch(
                        checked = colorTheme,
                        onCheckedChange = onColorClick
                    )
                }
            )
            ListItem(
                text = { Text(text = "Export data base to the Downloads folder") },
                trailing = {
                    TextButton(onClick = onExportDataBaseClick) { Text(text = "Export") }
                }
            )
            ListItem(
                text = { Text(text = "Import data base") },
                trailing = {
                    TextButton(onClick = onImportDataBaseClick) { Text(text = "Import") }
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            ListItem(
                text = { Text(text = "App version: ${BuildConfig.VERSION_NAME}") },
                secondaryText = { Text(text = "Build: ${BuildConfig.VERSION_CODE}") }
            )
        }
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



@Preview(showBackground = true)
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
private fun SettingsScreenLayoutPreviewData(){
    SettingsScreenLayout(
        title = "Settings",
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "")
            }
        },
        onExportDataBaseClick = {},
        darkTheme = false,
        colorTheme = true,
        onDarkClick = {},
        onImportDataBaseClick = {},
        onColorClick = {}
    )
}
