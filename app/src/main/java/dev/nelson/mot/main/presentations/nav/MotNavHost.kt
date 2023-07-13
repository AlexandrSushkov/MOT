package dev.nelson.mot.main.presentations.nav

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.nelson.mot.core.ui.MotNavBackIcon
import dev.nelson.mot.core.ui.MotNavDrawerIcon
import dev.nelson.mot.main.presentations.screen.categories_list.CategoryListScreen
import dev.nelson.mot.main.presentations.screen.category_details.CategoryDetailsScreen
import dev.nelson.mot.main.presentations.screen.settings.country_picker.CountryPickerScreen
import dev.nelson.mot.main.presentations.screen.dashboard.DashboardScreen
import dev.nelson.mot.main.presentations.screen.payment_details.PaymentDetailsScreen
import dev.nelson.mot.main.presentations.screen.payment_list.PaymentListScreen
import dev.nelson.mot.main.presentations.screen.settings.SettingsScreen
import dev.nelson.mot.main.presentations.screen.settings.app_theme.SelectAppThemeScreen
import dev.nelson.mot.main.presentations.screen.statistic.StatisticScreen
import dev.nelson.mot.main.presentations.screen.statistic.StatisticScreenExperemental
import dev.nelson.mot.main.util.constant.Constants
import kotlinx.coroutines.launch

@Composable
fun MotNavHost(
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
            route = Dashboard.route,
            content = {
                DashboardScreen(
                    appBarTitle = "dashboard",
                    appBarNavigationIcon = { MotNavDrawerIcon { coroutineScope.launch { navigationDrawerState.open() } } },
                )
            },
        )
        composable(
            route = Payments.route,
            content = {
                PaymentListScreen(
                    viewModel = hiltViewModel(),
                    navigationIcon = { MotNavDrawerIcon { coroutineScope.launch { navigationDrawerState.open() } } },
                    openPaymentDetails = { paymentId ->
                        paymentId?.let {
                            navController.navigate(
                                route = "${PaymentDetails.route}?${Constants.PAYMENT_ID_KEY}=$paymentId"
                            )
                        } ?: navController.navigate(route = PaymentDetails.route)
                    },
                    openPaymentDetailsForCategory = { categoryId ->
                        navController.navigate(
                            route = "${PaymentDetails.route}?${Constants.CATEGORY_ID_KEY}=$categoryId"
                        )
                    },
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
                        paymentId?.let {
                            navController.navigate(
                                route = "${PaymentDetails.route}?${Constants.PAYMENT_ID_KEY}=$paymentId"
                            )
                        }
                            ?: navController.navigate(route = PaymentDetails.route)
                    },
                    openPaymentDetailsForCategory = { categoryId ->
                        navController.navigate(
                            route = "${PaymentDetails.route}?${Constants.CATEGORY_ID_KEY}=$categoryId"
                        )
                    },
                )
            },
            arguments = listOf(navArgument(Constants.CATEGORY_ID_KEY) { type = NavType.IntType })
        )
        /**
         * Open payment details payment form payments list.
         */
        composable(
            route = "${PaymentDetails.route}?${Constants.PAYMENT_ID_KEY}={${Constants.PAYMENT_ID_KEY}}",
            content = {
                PaymentDetailsScreen(
                    viewModel = hiltViewModel(),
                    closeScreen = { navController.popBackStack() }
                )
            },
            arguments = listOf(navArgument(Constants.PAYMENT_ID_KEY) { type = NavType.IntType })
        )
        /**
         * Open new payment details payment form payments list for a selected category.
         */
        composable(
            route = "${PaymentDetails.route}?${Constants.CATEGORY_ID_KEY}={${Constants.CATEGORY_ID_KEY}}",
            content = {
                PaymentDetailsScreen(
                    viewModel = hiltViewModel(),
                    closeScreen = { navController.popBackStack() }
                )
            },
            arguments = listOf(navArgument(Constants.CATEGORY_ID_KEY) { type = NavType.IntType })
        )
        /**
         * Open payment details form widget.
         */
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
                    appBarNavigationIcon = {
                        MotNavDrawerIcon { coroutineScope.launch { navigationDrawerState.open() } }
                    },
                    openPaymentsByCategoryAction = { categoryId ->
                        navController.navigate("${Payments.route}?${Constants.CATEGORY_ID_KEY}=$categoryId")
                    }
                )
            },
        )
        composable(
            route = "${CategoryDetails.route}?${Constants.CATEGORY_ID_KEY}={${Constants.CATEGORY_ID_KEY}}",
            content = {
                CategoryDetailsScreen(
                    viewModel = hiltViewModel(),
                    closeScreen = { navController.popBackStack() }
                )
            },
            arguments = listOf(navArgument(Constants.CATEGORY_ID_KEY) { type = NavType.IntType })
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
            route = StatisticExperimental.route,
            content = {
                StatisticScreenExperemental(
                    viewModel = hiltViewModel(),
                    appBarTitle = "Statistic Old",
                    appBarNavigationIcon = { MotNavBackIcon { navController.popBackStack() } }
                )
            },
        )
        composable(
            route = Statistic.route,
            content = {
                StatisticScreen(
                    viewModel = hiltViewModel(),
                    appBarTitle = "Statistic",
                    navigationIcon = { MotNavBackIcon { navController.popBackStack() } },
                    openPaymentsByCategoryAction = { categoryId ->
                        navController.navigate("${Payments.route}?${Constants.CATEGORY_ID_KEY}=$categoryId")
                    }
                )
            },
        )
        composable(
            route = Settings.route,
            content = {
                SettingsScreen(
                    title = Settings.route,
                    settingsViewModel = hiltViewModel(),
                    navigationIcon = { MotNavDrawerIcon { coroutineScope.launch { navigationDrawerState.open() } } },
                    openCountryPickerScreen = { navController.navigate(CountryPicker.route) },
                    openAppThemePickerScreen = { navController.navigate(AppThemePicker.route) },
                )
            },
        )
        composable(
            route = CountryPicker.route,
            content = {
                CountryPickerScreen(
                    countryPickerViewModel = hiltViewModel(),
                    closeScreenAction = { navController.popBackStack() }
                )
            },
        )
        composable(
            route = AppThemePicker.route,
            content = {
                SelectAppThemeScreen(
                    selectAppThemeViewModel = hiltViewModel(),
                    closeScreenAction = { navController.popBackStack() },
                )
            },
        )
    }
}