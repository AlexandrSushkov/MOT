package dev.nelson.mot.main.presentations.screen.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dev.nelson.mot.main.BuildConfig
import dev.nelson.mot.main.presentations.ui.theme.MotTheme
import dev.nelson.mot.main.presentations.widgets.TopAppBarMot
import dev.nelson.mot.main.presentations.ui.theme.colorsMaterial3
import dev.nelson.mot.main.util.StringUtils

@Composable
fun SettingsScreen(
    title: String,
    settingsViewModel: SettingsViewModel,
    navigationIcon: @Composable () -> Unit = {},
) {
    val toastMessage by settingsViewModel.showToastAction.collectAsState(StringUtils.EMPTY)
    val darkTheme by settingsViewModel.darkThemeSwitchState.collectAsState(false)
    val colorTheme by settingsViewModel.dynamicColorThemeSwitchState.collectAsState(false)

    SettingsScreenLayout(
        title = title,
        navigationIcon = navigationIcon,
        onExportDataBaseClick = { settingsViewModel.onExportDataBaseClick() },
        toastMessage = toastMessage,
        darkTheme = darkTheme,
        colorTheme = colorTheme,
        onDarkClick = { isChecked -> settingsViewModel.onDarkThemeCheckedChange(isChecked) },
        onColorClick = { isChecked -> settingsViewModel.onDynamicColorThemeCheckedChange(isChecked) }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenLayout(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    onExportDataBaseClick: () -> Unit,
    toastMessage: String,
    darkTheme: Boolean,
    colorTheme: Boolean,
    onDarkClick: (Boolean) -> Unit,
    onColorClick: (Boolean) -> Unit,

    ) {

    if (toastMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = { TopAppBarMot(title = title, navigationIcon = navigationIcon) }
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
            // TODO: show only for android 12+
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
                trailing = { TextButton(onClick = onExportDataBaseClick) { Text(text = "Export") } },
                text = { Text(text = "Export data base to the Downloads folder") }
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
        toastMessage = "",
        darkTheme = false,
        colorTheme = true,
        onDarkClick = {},
        onColorClick = {}
    )
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
            toastMessage = "",
            darkTheme = false,
            colorTheme = true,
            onDarkClick = {},
            onColorClick = {}
        )
    }
}
