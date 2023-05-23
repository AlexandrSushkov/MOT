package dev.nelson.mot.main.presentations.nav

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.utils.preview.MotPreview

@Composable
fun NavigationDrawerContent(
    navController: NavHostController,
    drawerViewState: DrawerViewState,
    closeNavDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        drawerViewState.drawerItems.forEach { drawerItem ->
            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = {
                    Icon(
                        drawerItem.icon, contentDescription = "${drawerItem.route} drawer item icon"
                    )
                },
                label = { Text(text = drawerItem.route) },
                onClick = {
                    if (navController.currentDestination?.route != drawerItem.route) {
                        val isRouteInTheBackStack = navController.backQueue
                            .any { it.destination.route == drawerItem.route }
                        if (isRouteInTheBackStack) {
                            navController.popBackStack(drawerItem.route, inclusive = false)
                        } else {
                            navController.navigate(drawerItem.route)
                        }
                    }
                    closeNavDrawer.invoke()
                },
                selected = drawerViewState.selectedItem == drawerItem.route
            )
        }
    }
}

@MotPreview
@Composable
private fun NavigationDrawerContentPreview() {
    MotMaterialTheme {
        NavigationDrawerContent(
            navController = NavHostController(LocalContext.current),
            drawerViewState = DrawerViewState(),
            closeNavDrawer = {}
        )
    }
}