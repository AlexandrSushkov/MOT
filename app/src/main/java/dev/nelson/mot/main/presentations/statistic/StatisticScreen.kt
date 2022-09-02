package dev.nelson.mot.main.presentations.statistic

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import dev.nelson.mot.main.presentations.motTabRowScreens
import dev.nelson.mot.main.presentations.widgets.LineChartMot

@Composable
fun StatisticScreen(navHostController: NavHostController) {
    Scaffold(
        bottomBar = {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                BottomNavigation() {
                    motTabRowScreens.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                            label = { Text(screen.route) },
                            selected = navHostController.currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {}
                        )
                    }
                }
            }
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Row {
                    NavigationRail() {
                        motTabRowScreens.forEach { screen ->
                            NavigationRailItem(
                                label = { Text(screen.route) },
                                icon = { Icon(screen.icon, contentDescription = "") },
                                selected = navHostController.currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = { }
                            )
                        }

                    }
                    Column {
                        StatisticContent()
                    }
                }
            } else {
                StatisticContent()
            }
        }
    }
}

@Composable
fun StatisticContent() {
    Box(modifier = Modifier.padding(16.dp, vertical = 24.dp)) {
        LineChartMot(items = listOf(0.2f, 0.5f, 0.1f, 0.3f))
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticScreenPreview() {
    StatisticScreen(NavHostController(LocalContext.current))
}

