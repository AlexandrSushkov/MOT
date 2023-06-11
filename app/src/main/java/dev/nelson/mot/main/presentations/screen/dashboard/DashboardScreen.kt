package dev.nelson.mot.main.presentations.screen.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.utils.preview.MotPreview

@Composable
fun DashboardScreen(
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit = {}
) {
    DashboardScreenLayout()
}

@Composable
fun DashboardScreenLayout() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = innerPadding),
        ) {
            TabLayout()
        }

    }
}

@MotPreview
@Composable
private fun DashboardScreenLayoutPreview() {
    MotMaterialTheme {
        DashboardScreenLayout()
    }
}

@Composable
fun TabLayout() {
    val tabs = listOf("Tab 1", "Tab 2", "Tab 3")
    val selectedTab = remember { mutableStateOf(0) }
    TabRow(
        selectedTabIndex = selectedTab.value,
        modifier = Modifier.fillMaxWidth(),
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.value]),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab.value == index,
                onClick = { selectedTab.value = index },
                text = {
                    Text(
                        text = title,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    }

    // Content for each tab
    when (selectedTab.value) {
        0 -> TabContent("Content for Tab 1")
        1 -> TabContent("Content for Tab 2")
        2 -> TabContent("Content for Tab 3")
    }
}

@Composable
fun TabContent(s: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = s)
    }
}
