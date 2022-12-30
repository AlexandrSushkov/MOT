@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.home

import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.presentations.nav.Categories
import dev.nelson.mot.main.presentations.nav.CategoryDetails
import dev.nelson.mot.main.presentations.nav.PaymentDetails
import dev.nelson.mot.main.presentations.nav.Payments
import dev.nelson.mot.main.presentations.nav.Settings
import dev.nelson.mot.main.presentations.nav.Statistic
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

            MotTheme(forceDark = forceDark, dynamicColor = dynamicColor) {
                MotApp(
                    isOpenedFromWidget = isOpenedFromWidget,
                    finishAction = { finishAndRemoveTask() }
                )
            }
        }
    }
}

// TODO: move to navigation
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MotApp(isOpenedFromWidget: Boolean, finishAction: () -> Unit) {

    val navController = rememberNavController()
    val currNavState by navController.currentBackStackEntryAsState()
    val navigationDrawerValue = if (LocalInspectionMode.current) DrawerValue.Open else DrawerValue.Closed
    val navigationDrawerState = rememberDrawerState(navigationDrawerValue)
    val scope = rememberCoroutineScope()

    fun getItemBackgroundByRoute(route: String): Color {
        return if ((navController.currentDestination as NavDestination).route == route) Color.LightGray else Color.White
    }

    BackHandler(
        enabled = navigationDrawerState.isOpen,
        onBack = { scope.launch { navigationDrawerState.close() } }
    )

    BackHandler(
        enabled = isOpenedFromWidget,
        onBack = { scope.launch { finishAction.invoke() } }
    )

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            NavigationDrawer(
                drawerState = navigationDrawerState,
                gesturesEnabled = navigationDrawerState.isOpen,
                drawerContent = {
                    Spacer(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(0.05f)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(0.5f)
                    ) {
                        val drawerItemModifier = Modifier
//                                .background(Color.LightGray, RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50))

//                                val paymentColor by animateColorAsState(getItemBackgroundByRoute(Payments.route))
//                                val categoryColor by animateColorAsState(getItemBackgroundByRoute(Categories.route))
                        val statisticColor = getItemBackgroundByRoute(Statistic.route)

                        ListItem(
                            modifier = drawerItemModifier
                                .selectable(
                                    selected = (navController.currentDestination as NavDestination).route == Payments.route,
                                    onClick = {
                                        if ((navController.currentDestination as NavDestination).route != Payments.route) {
                                            navController.popBackStack(Payments.route, false)
                                        }
                                        scope.launch { navigationDrawerState.close() }
                                    }
                                ),
                            icon = { Icon(Payments.icon, contentDescription = "${Payments::class.java} drawer item") },
                            text = { Text(text = Payments.route) }
                        )
                        ListItem(
                            modifier = drawerItemModifier
                                .selectable(
                                    selected = (navController.currentDestination as NavDestination).route == Categories.route,
                                    onClick = {
                                        if ((navController.currentDestination as NavDestination).route != Categories.route) {
                                            navController.navigate(Categories.route)
                                        }
                                        scope.launch { navigationDrawerState.close() }
                                    },
                                ),
                            icon = { Icon(Categories.icon, contentDescription = "${Categories::class.java} drawer item") },
                            text = { Text(text = Categories.route) }
                        )
                        ListItem(
                            modifier = drawerItemModifier
                                .selectable(
                                    selected = (navController.currentDestination as NavDestination).route == Statistic.route,
                                    onClick = {
                                        if ((navController.currentDestination as NavDestination).route != Statistic.route) {
                                            navController.navigate(Statistic.route)
                                        }
                                        scope.launch { navigationDrawerState.close() }
                                    },
                                ),
                            icon = { Icon(Statistic.icon, contentDescription = "${Categories::class.java} drawer item") },
                            text = { Text(text = Statistic.route) }
                        )
                    }
                },
            ) {
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
                                navigationIcon = { MotNavDrawerIcon { scope.launch { navigationDrawerState.open() } } },
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
                                navigationIcon = { MotNavDrawerIcon { scope.launch { navigationDrawerState.open() } } },
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
        }
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
