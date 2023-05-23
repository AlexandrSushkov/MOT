package dev.nelson.mot.main.presentations.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
                selected = drawerViewState.selectedItem == drawerItem.route,
//                badge = {
//                    Box(
//                        modifier = Modifier
//                            .size(24.dp)
//                            .background(
//                                color = MaterialTheme.colorScheme.primaryContainer,
//                                shape = RoundedCornerShape(30.dp)
//                            )
//                    ) {
//                        Text(
//                            modifier = Modifier.align(Alignment.Center),
//                            text = "1"
//                        )
//                    }
//                }
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
