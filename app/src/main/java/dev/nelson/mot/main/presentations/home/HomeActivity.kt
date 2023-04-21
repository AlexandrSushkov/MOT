@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.home

import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.presentations.nav.Categories
import dev.nelson.mot.main.presentations.nav.CategoryDetails
import dev.nelson.mot.main.presentations.nav.MotDestinations
import dev.nelson.mot.main.presentations.nav.PaymentDetails
import dev.nelson.mot.main.presentations.nav.Payments
import dev.nelson.mot.main.presentations.nav.Settings
import dev.nelson.mot.main.presentations.nav.Statistic
import dev.nelson.mot.main.presentations.nav.drawerItemsList
import dev.nelson.mot.main.presentations.screen.categories_list.CategoryListScreen
import dev.nelson.mot.main.presentations.screen.category_details.CategoryDetailsScreen
import dev.nelson.mot.main.presentations.screen.payment_details.PaymentDetailsScreen
import dev.nelson.mot.main.presentations.screen.payment_list.PaymentListScreen
import dev.nelson.mot.main.presentations.screen.settings.SettingsScreen
import dev.nelson.mot.main.presentations.screen.statistic.StatisticScreen
import dev.nelson.mot.main.presentations.ui.theme.MotTheme
import dev.nelson.mot.main.presentations.widgets.MotNavBackIcon
import dev.nelson.mot.main.presentations.widgets.MotNavDrawerIcon
import dev.nelson.mot.main.presentations.widgets.MotNavSettingsIcon
import dev.nelson.mot.main.util.Constants
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val splashScreenViewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isOpenedFromWidget = intent?.extras?.getBoolean(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, false) ?: false

        installSplashScreen().apply {
            setKeepOnScreenCondition { splashScreenViewModel.isLoading.value }
        }

        setContent {
            val forceDark by splashScreenViewModel.darkThemeEnabled.collectAsState(false)
            val dynamicColor by splashScreenViewModel.dynamicColorEnabled.collectAsState(false)

            MotTheme(
                forceDark = forceDark,
                dynamicColor = dynamicColor
            ) {
                MotApp(
                    isOpenedFromWidget = isOpenedFromWidget,
                    finishAction = { finishAndRemoveTask() }
                )
            }
        }
    }
}

// TODO: move to navigation
@Composable
fun MotApp(isOpenedFromWidget: Boolean, finishAction: () -> Unit) {

    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navigationDrawerValue = if (LocalInspectionMode.current) DrawerValue.Open else DrawerValue.Closed
    val navigationDrawerState = rememberDrawerState(navigationDrawerValue)
    val drawerItems = drawerItemsList
    val selectedRoute = remember { mutableStateOf(drawerItems.first().route) }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        destination.route?.let { currentRoute ->
            if (drawerItems.map { drawerItem -> drawerItem.route }.contains(currentRoute)) {
                selectedRoute.value = currentRoute
            }
        }
    }

    /**
     * Close the navigation drawer back handler.
     */
    BackHandler(
        enabled = navigationDrawerState.isOpen,
        onBack = { coroutineScope.launch { navigationDrawerState.close() } }
    )

    /**
     * Close the app back handler.
     */
    BackHandler(
        enabled = isOpenedFromWidget,
        onBack = { coroutineScope.launch { finishAction.invoke() } }
    )

    Scaffold { innerPadding ->
        ModalNavigationDrawer(
            drawerState = navigationDrawerState,
            modifier = Modifier.padding(innerPadding),
            gesturesEnabled = navigationDrawerState.isOpen,
            drawerContent = {
                NavigationDrawerContent(
                    navController = navController,
                    drawerItems = drawerItems,
                    selectedRoute = selectedRoute
                ) { coroutineScope.launch { navigationDrawerState.close() } }
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
    }
}

@Composable
private fun NavigationDrawerContent(
    navController: NavHostController,
    drawerItems: List<MotDestinations>,
    selectedRoute: MutableState<String>,
    closeNavDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        drawerItems.forEach { drawerItem ->
            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = { Icon(drawerItem.icon, contentDescription = "${drawerItem.route} drawer item") },
                label = { Text(text = drawerItem.route) },
                onClick = {
                    if (navController.currentDestination?.route != drawerItem.route) {
                        navController.navigate(drawerItem.route)
                    }
                    closeNavDrawer.invoke()
                },
                selected = selectedRoute.value == drawerItem.route
            )
        }
    }
}

@Composable
private fun MotNavHost(
    navController: NavHostController,
    navigationDrawerState: DrawerState,
    isOpenedFromWidget: Boolean,
    finishAction: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val startDestination = if (isOpenedFromWidget) PaymentDetails else Payments

    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(
            route = Payments.route,
            content = {
                PaymentListScreen(
                    viewModel = hiltViewModel(),
                    navigationIcon = { MotNavDrawerIcon { coroutineScope.launch { navigationDrawerState.open() } } },
                    openPaymentDetails = { paymentId ->
                        paymentId?.let { navController.navigate(route = "${PaymentDetails.route}?id=$paymentId") }
                            ?: navController.navigate(route = PaymentDetails.route)
                    },
                    settingsIcon = { MotNavSettingsIcon { navController.navigate(Settings.route) } }
                )
            },
        )
        composable(
            route = "${Payments.route}?${Constants.CATEGORY_ID_KEY}={${Constants.CATEGORY_ID_KEY}}",
            content = {
                PaymentListScreen(
                    viewModel = hiltViewModel(),
                    navigationIcon = { MotNavBackIcon { navController.popBackStack() } },
                    openPaymentDetails = { paymentId ->
                        paymentId?.let { navController.navigate(route = "${PaymentDetails.route}?id=$paymentId") }
                            ?: navController.navigate(route = PaymentDetails.route)
                    },
                    settingsIcon = { MotNavSettingsIcon { navController.navigate(Settings.route) } }
                )
            },
            arguments = listOf(navArgument(Constants.CATEGORY_ID_KEY) { type = NavType.IntType })
        )
        composable(
            route = "${PaymentDetails.route}?id={id}",
            content = {
                PaymentDetailsScreen(
                    viewModel = hiltViewModel(),
                    closeScreen = { navController.popBackStack() }
                )
            },
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        )
        composable(
            route = PaymentDetails.route,
            content = {
                PaymentDetailsScreen(
                    viewModel = hiltViewModel(),
                    closeScreen = { if (isOpenedFromWidget) finishAction.invoke() else navController.popBackStack() })
            }
        )
        composable(
            route = Categories.route,
            content = {
                CategoryListScreen(
                    viewModel = hiltViewModel(),
                    title = Categories.route,
                    navigationIcon = { MotNavDrawerIcon { coroutineScope.launch { navigationDrawerState.open() } } },
                    openCategoryDetails = { categoryId ->
                        categoryId?.let { navController.navigate("${CategoryDetails.route}?id=$categoryId") } ?: navController.navigate(
                            CategoryDetails.route
                        )
                    },
                    openPaymentsByCategory = { categoryId ->
                        categoryId?.let { navController.navigate("${Payments.route}?category_id=$categoryId") } ?: navController.popBackStack()
                    },
                    settingsIcon = { MotNavSettingsIcon { navController.navigate(Settings.route) } })
            }
        )
        composable(
            route = "${CategoryDetails.route}?id={id}",
            content = {
                CategoryDetailsScreen(
                    viewModel = hiltViewModel(),
                    closeScreen = { navController.popBackStack() }
                )
            },
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        )
        composable(
            route = CategoryDetails.route,
            content = {
                CategoryDetailsScreen(
                    viewModel = hiltViewModel(),
                    closeScreen = { navController.popBackStack() }
                )
            },
        )
        composable(
            route = Statistic.route,
            content = {
                StatisticScreen(
                    viewModel = hiltViewModel(),
                    navHostController = navController
                )
            },
        )
        composable(
            route = Settings.route,
            content = {
                SettingsScreen(
                    title = Settings.route,
                    settingsViewModel = hiltViewModel(),
                    navigationIcon = { MotNavBackIcon { navController.popBackStack() } }
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MotAppLightPreview() {
    MotApp(
        isOpenedFromWidget = false,
        finishAction = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MotAppDarkPreview() {
    MotTheme(darkTheme = true) {
        MotApp(
            isOpenedFromWidget = false,
            finishAction = {}
        )
    }
}
