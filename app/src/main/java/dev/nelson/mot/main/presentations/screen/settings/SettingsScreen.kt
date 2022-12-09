package dev.nelson.mot.main.presentations.screen.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dev.nelson.mot.main.BuildConfig
import dev.nelson.mot.main.util.StringUtils

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    navigationIcon: @Composable () -> Unit = {},
) {
    val toastMessage by settingsViewModel.showToastAction.collectAsState(StringUtils.EMPTY)

    SettingsScreenLayout(
        navigationIcon = navigationIcon,
        onExportDataBaseClick = { settingsViewModel.onExportDataBaseClick() },
        toastMessage = toastMessage
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SettingsScreenLayout(
    navigationIcon: @Composable () -> Unit = {},
    onExportDataBaseClick: () -> Unit,
    toastMessage: String,
) {

    if (toastMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = navigationIcon,
                title = { Text(text = "Settings") },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            ListItem(
                trailing = {
                    Switch(checked = false, onCheckedChange = {})
                }) {
                Text(text = "Dark theme")
            }
            ListItem(
                trailing = {
                    TextButton(onClick = onExportDataBaseClick) { Text(text = "Export") }
                }) {
                Text(text = "Export data base to the Downloads folder")
            }
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
private fun SettingsScreenLayoutPreview() {
    SettingsScreenLayout(
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "back icon")
            }
        },
        onExportDataBaseClick = {},
        toastMessage = "",
    )
}
