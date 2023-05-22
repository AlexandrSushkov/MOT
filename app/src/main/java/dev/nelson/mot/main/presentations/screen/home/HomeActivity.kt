@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.screen.home

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
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.core.ui.MotMaterialTheme
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
import dev.nelson.mot.core.ui.MotNavBackIcon
import dev.nelson.mot.core.ui.MotNavDrawerIcon
import dev.nelson.mot.core.ui.MotNavSettingsIcon
import dev.nelson.mot.core.ui.view_state.AppThemeViewState
import dev.nelson.mot.main.presentations.nav.CountryPicker
import dev.nelson.mot.main.presentations.screen.country_picker.CountryPickerScreen
import dev.nelson.mot.main.util.constant.Constants
import dev.utils.preview.MotPreview
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val splashScreenViewModel: SplashScreenViewModel by viewModels()
    private val motThemeViewModel: MotThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isOpenedFromWidget = intent
            ?.extras
            ?.getBoolean(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, false) ?: false

        installSplashScreen().apply {
            setKeepOnScreenCondition { splashScreenViewModel.isLoading.value }
        }

        setContent {
            val appThemeViewState by motThemeViewModel.appThemeViewState.collectAsState()

            MotMaterialTheme(appThemeViewState) {
                MotApp(
                    isOpenedFromWidget = isOpenedFromWidget,
                    finishAction = { finishAndRemoveTask() }
                )
            }
        }
        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Timber.d("config keys: ${remoteConfig.all.keys}")
//                    Toast.makeText(
//                        this,
//                        "Fetch and activate succeeded",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Fetch failed",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                }
                splashScreenViewModel.onRemoteConfigFetched()
            }
    }
}

// TODO: move to navigation
@Composable
fun MotApp(isOpenedFromWidget: Boolean, finishAction: () -> Unit) {

    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navigationDrawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerItems = drawerItemsList
    val selectedRoute = remember { mutableStateOf(drawerItems.first().route) }

    /**
     * Close the app back handler.
     */
    BackHandler(
        enabled = isOpenedFromWidget,
        onBack = { coroutineScope.launch { finishAction.invoke() } }
    )

    navController.addOnDestinationChangedListener { _, destination, _ ->
        destination.route?.let { currentRoute ->
            drawerItems.filter { drawerItem -> drawerItem.route == currentRoute }
                .map { selectedRoute.value = it.route }
        }
    }

    Scaffold { innerPadding ->
        ModalNavigationDrawer(
            drawerState = navigationDrawerState,
            modifier = Modifier.padding(innerPadding),
            gesturesEnabled = navigationDrawerState.isOpen,
            drawerContent = {
                NavigationDrawerContent(
                    navController = navController,
                    drawerItems = drawerItems,
                    selectedRoute = selectedRoute,
                    closeNavDrawer = { coroutineScope.launch { navigationDrawerState.close() } }
                )
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
                selected = selectedRoute.value == drawerItem.route
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MotNavHost(
    navController: NavHostController,
    navigationDrawerState: DrawerState,
    isOpenedFromWidget: Boolean,
    finishAction: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val startDestination = if (isOpenedFromWidget) PaymentDetails else Payments

    /**
     * Close the navigation drawer back handler.
     * DO NOT move this handler outside of the NavHost! It will not work.
     */
    BackHandler(
        enabled = navigationDrawerState.isOpen,
        onBack = { coroutineScope.launch { navigationDrawerState.close() } }
    )

    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(
            route = Payments.route,
            content = {
                PaymentListScreen(viewModel = hiltViewModel(),
                    navigationIcon = { MotNavDrawerIcon { coroutineScope.launch { navigationDrawerState.open() } } },
                    openPaymentDetails = { paymentId ->
                        paymentId?.let { navController.navigate(route = "${PaymentDetails.route}?id=$paymentId") }
                            ?: navController.navigate(route = PaymentDetails.route)
                    },
                    settingsIcon = { MotNavSettingsIcon { navController.navigate(Settings.route) } })
            },
        )
        composable(
            route = "${Payments.route}?${Constants.CATEGORY_ID_KEY}={${Constants.CATEGORY_ID_KEY}}",
            content = {
                PaymentListScreen(viewModel = hiltViewModel(),
                    navigationIcon = { MotNavBackIcon { navController.popBackStack() } },
                    openPaymentDetails = { paymentId ->
                        paymentId?.let { navController.navigate(route = "${PaymentDetails.route}?id=$paymentId") }
                            ?: navController.navigate(route = PaymentDetails.route)
                    },
                    settingsIcon = { MotNavSettingsIcon { navController.navigate(Settings.route) } })
            },
            arguments = listOf(navArgument(Constants.CATEGORY_ID_KEY) { type = NavType.IntType })
        )
        composable(
            route = "${PaymentDetails.route}?id={id}",
            content = {
                PaymentDetailsScreen(viewModel = hiltViewModel(),
                    closeScreen = { navController.popBackStack() })
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
                    appBarNavigationIcon = { MotNavDrawerIcon { coroutineScope.launch { navigationDrawerState.open() } } },
                    actionsIcons = { MotNavSettingsIcon { navController.navigate(Settings.route) } },
                    openPaymentsByCategoryAction = { categoryId ->
                        categoryId?.let { navController.navigate("${Payments.route}?category_id=$categoryId") }
                            ?: navController.popBackStack()
                    })
            },
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
                    navigationIcon = { MotNavBackIcon { navController.popBackStack() } },
                    openCountryPickerScreen = { navController.navigate(CountryPicker.route) },
                )
            },
        )
        composable(
            route = CountryPicker.route,
            content = {
                CountryPickerScreen(
                    viewModel = hiltViewModel()
                ) { navController.popBackStack() }
            },
        )
    }
}

@MotPreview
@Composable
private fun MotAppDarkPreview() {
    MotMaterialTheme {
        MotApp(isOpenedFromWidget = false, finishAction = {})
    }
}
