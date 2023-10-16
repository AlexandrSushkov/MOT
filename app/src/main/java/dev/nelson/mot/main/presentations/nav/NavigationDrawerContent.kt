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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.utils.preview.MotPreview

@Composable
fun NavigationDrawerContent(
    modifier: Modifier = Modifier, // default modifier is used to fix preview
    drawerViewState: DrawerViewState,
    onItemClick: (MotDestination) -> Unit = {}
) {
    ModalDrawerSheet(
        modifier = modifier,
        drawerShape = RectangleShape
    ) {
        Spacer(Modifier.height(12.dp))
        drawerViewState.motDrawerItems.forEach { motDrawerItem ->
            if (motDrawerItem.isAvailable) {
                val destination = motDrawerItem.destination
                NavigationDrawerItem(
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    icon = {
                        Icon(
                            destination.icon,
                            contentDescription = "${destination.route} drawer item icon"
                        )
                    },
                    label = { Text(text = destination.route) },
                    onClick = { onItemClick.invoke(destination) },
//                    if (navController.currentDestination?.route != drawerItem.route) {
//                        val isRouteInTheBackStack = navController.backQueue
//                            .any { it.destination.route == drawerItem.route }
//                        if (isRouteInTheBackStack) {
//                            navController.popBackStack(drawerItem.route, inclusive = false)
//                        } else {
//                            navController.navigate(drawerItem.route)
//                        }
//                    }
//                    closeNavDrawer.invoke()
//                },
                    selected = drawerViewState.selectedItem == destination.route
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
}

@MotPreview
@Composable
private fun NavigationDrawerContentPreview() {
    MotMaterialTheme {
        NavigationDrawerContent(
            modifier = Modifier,
            drawerViewState = DrawerViewState(),
            onItemClick = {}
        )
    }
}
