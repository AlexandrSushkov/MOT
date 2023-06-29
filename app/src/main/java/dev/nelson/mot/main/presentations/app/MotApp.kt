package dev.nelson.mot.main.presentations.app

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.presentations.nav.DrawerViewModel
import dev.nelson.mot.main.presentations.nav.DrawerViewState
import dev.nelson.mot.main.presentations.nav.MotNavHost
import dev.nelson.mot.main.presentations.nav.NavigationDrawerContent
import dev.utils.preview.MotPreview
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
@Composable
fun MotApp(isOpenedFromWidget: Boolean, finishAction: () -> Unit) {

    val drawerViewModel = hiltViewModel<DrawerViewModel>()
    val navigationDrawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerViewState by drawerViewModel.drawerViewState.collectAsState(DrawerViewState())
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()

    /**
     * Close the app back handler.
     */
    BackHandler(
        enabled = isOpenedFromWidget,
        onBack = { coroutineScope.launch { finishAction.invoke() } }
    )

    navController.addOnDestinationChangedListener { _, destination, _ ->
        destination.route?.let { currentRoute -> drawerViewModel.onDestinationChanged(currentRoute) }
    }

    val currentBackStack by navController.currentBackStack.collectAsState()

    MotAppLayout(
        isOpenedFromWidget,
        finishAction,
        navigationDrawerState,
        drawerViewState,
        navController,
        currentBackStack
    )
}

@SuppressLint("RestrictedApi")
@Composable
private fun MotAppLayout(
    isOpenedFromWidget: Boolean,
    finishAction: () -> Unit,
    navigationDrawerState: DrawerState,
    drawerViewState: DrawerViewState,
    navController: NavHostController,
    currentBackStack: List<NavBackStackEntry>
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ModalNavigationDrawer(
                drawerState = navigationDrawerState,
                modifier = Modifier.padding(innerPadding),
                gesturesEnabled = navigationDrawerState.isOpen,
                drawerContent = {
                    NavigationDrawerContent(
                        drawerViewState = drawerViewState
                    ) { destination ->
                        if (navController.currentDestination?.route != destination.route) {
                            val isRouteInTheBackStack =
                                currentBackStack.any { it.destination.route == destination.route }
                            if (isRouteInTheBackStack) {
                                navController.popBackStack(
                                    destination.route,
                                    inclusive = false
                                )
                            } else {
                                navController.navigate(destination.route)
                            }
                        }
                        coroutineScope.launch { navigationDrawerState.close() }
                    }
                },
                content = {
                    MotNavHost(
                        navController = navController,
                        navigationDrawerState = navigationDrawerState,
                        isOpenedFromWidget = isOpenedFromWidget,
                        finishAction = finishAction
                    )
                }
            )
        } else {
            Row {
                NavigationRail {
                    drawerViewState.motDrawerItems.forEach { motDrawerItem ->
                        if (motDrawerItem.isAvailable) {
                            val destination = motDrawerItem.destination
                            NavigationRailItem(
                                label = { Text(destination.route) },
                                icon = { Icon(destination.icon, contentDescription = "") },
                                selected = drawerViewState.selectedItem == destination.route,
                                onClick = {
                                    if (navController.currentDestination?.route != destination.route) {
                                        val isRouteInTheBackStack = currentBackStack
                                            .any { it.destination.route == destination.route }
                                        if (isRouteInTheBackStack) {
                                            navController.popBackStack(
                                                destination.route,
                                                inclusive = false
                                            )
                                        } else {
                                            navController.navigate(destination.route)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
                MotNavHost(
                    navController = navController,
                    navigationDrawerState = navigationDrawerState,
                    isOpenedFromWidget = isOpenedFromWidget,
                    finishAction = finishAction
                )
            }
        }
    }
}

@MotPreview
@Composable
private fun MotAppLayoutPreview() {
    MotMaterialTheme {
        MotAppLayout(
            isOpenedFromWidget = false,
            finishAction = {},
            navigationDrawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            drawerViewState = DrawerViewState(),
            navController = rememberNavController(),
            currentBackStack = emptyList()
        )
    }
}
