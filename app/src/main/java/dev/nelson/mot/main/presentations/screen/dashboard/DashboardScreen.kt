package dev.nelson.mot.main.presentations.screen.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotNavDrawerIcon
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.utils.preview.MotPreview
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit = {}
) {
    DashboardScreenLayout(
        appBarTitle = appBarTitle,
        appBarNavigationIcon = appBarNavigationIcon
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreenLayout(
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            MotTopAppBar(
                appBarTitle = appBarTitle,
                navigationIcon = appBarNavigationIcon
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Center),
                text = appBarTitle,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@MotPreview
@Composable
private fun DashboardScreenLayoutPreview() {
    MotMaterialTheme {
        DashboardScreenLayout(
            appBarTitle = "Dashboard",
            appBarNavigationIcon = {
                MotNavDrawerIcon { }
            }
        )
    }
}
