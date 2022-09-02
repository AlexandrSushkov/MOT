package dev.nelson.mot.main.presentations.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.presentations.categories_list.CategoryListScreen
import dev.nelson.mot.main.presentations.category_details.CategoryDetailsScreen
import dev.nelson.mot.main.presentations.nav.Categories
import dev.nelson.mot.main.presentations.nav.Payments
import dev.nelson.mot.main.presentations.nav.Statistic
import dev.nelson.mot.main.presentations.payment.PaymentDetailsScreen
import dev.nelson.mot.main.presentations.payment_list.PaymentListScreen
import dev.nelson.mot.main.presentations.statistic.StatisticScreen
import dev.nelson.mot.main.presentations.ui.theme.MotTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, MainActivityCompose::class.java)
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App() {
    MotTheme {

        val navController = rememberNavController()
        val drawerValue = if (LocalInspectionMode.current) DrawerValue.Open else DrawerValue.Closed
        val drawerState = rememberDrawerState(drawerValue)
        val scope = rememberCoroutineScope()

        fun getItemBackgroundByRoute(route: String): Color {
            return if ((navController.currentDestination as NavDestination).route == route) {
                Color.LightGray
            } else {
                Color.White
            }
        }
        BackHandler(enabled = true, onBack = {
            if (drawerState.isOpen){
                scope.launch { drawerState.close() }
            }
        })
        Scaffold() { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                ModalDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = drawerState.isOpen,
                    drawerContent = {
                        Box(
                            modifier = Modifier
//                                .background(Color.LightGray)
                                .fillMaxSize()
                                .weight(0.2f)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(0.5f)
                        ) {
                            val drawerItemModifier = Modifier
//                                .background(Color.LightGray, RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50))

                            val paymentColor by animateColorAsState(getItemBackgroundByRoute(Payments.route))
                            val categoryColor by animateColorAsState(getItemBackgroundByRoute(Categories.route))
                            val statisticColor = getItemBackgroundByRoute(Statistic.route)
                            ListItem(
                                modifier = drawerItemModifier
                                    .background(paymentColor)
                                    .clickable {
                                        if ((navController.currentDestination as NavDestination).route != Payments.route) {
                                            navController.popBackStack(Payments.route, false)
                                        }
                                        scope.launch { drawerState.close() }
                                    },
                                icon = { Icon(Payments.icon, contentDescription = "PaymentListScreen drawer item") },
                                text = { Text(text = Payments.route) }
                            )
                            ListItem(
                                modifier = drawerItemModifier
                                    .background(categoryColor)
                                    .clickable(
                                        onClick = {
                                            if ((navController.currentDestination as NavDestination).route != Categories.route) {
                                                navController.navigate(Categories.route)
                                            }
                                            scope.launch { drawerState.close() }
                                        }
                                    ),
                                icon = { Icon(Categories.icon, contentDescription = "CategoryListScreen drawer item") },
                                text = { Text(text = Categories.route) }
                            )
                            ListItem(
                                modifier = drawerItemModifier
//                                    .background(statisticColor)
                                    .clickable(
                                        onClick = {
                                            if ((navController.currentDestination as NavDestination).route != Statistic.route) {
                                                navController.navigate(Statistic.route)
                                            }
                                            scope.launch { drawerState.close() }
                                        }
                                    ),
                                icon = { Icon(Statistic.icon, contentDescription = "CategoryListScreen drawer item") },
                                text = { Text(text = Statistic.route) }
                            )
                        }
                    },
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Payments.route
                    ) {
                        composable(
                            route = Payments.route,
                            content = {
                                PaymentListScreen(
                                    openDrawer = { scope.launch { drawerState.open() } },
                                    openPaymentDetails = { paymentId ->
                                        paymentId?.let { navController.navigate(route = "PaymentDetailsScreen?id=$paymentId") }
                                            ?: navController.navigate(route = "PaymentDetailsScreen")


                                    })
                            },
                        )
                        composable(
                            route = "PaymentDetailsScreen?id={id}",
                            content = { PaymentDetailsScreen(closeScreen = { navController.popBackStack() }) },
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        )
                        composable(
                            route = "PaymentDetailsScreen",
                            content = { PaymentDetailsScreen(closeScreen = { navController.popBackStack() }) },
                        )
                        composable(
                            route = Categories.route,
                            content = {
                                CategoryListScreen(
                                    openDrawer = { scope.launch { drawerState.open() } },
                                    openCategoryDetails = { categoryId ->
                                        categoryId?.let { navController.navigate("CategoryDetailsScreen?id=$categoryId") }
                                            ?: navController.navigate("CategoryDetailsScreen")
                                    },
                                    openPaymentsByCategory = { }
                                )
                            }
                        )
                        composable(
                            route = "CategoryDetailsScreen?id={id}",
                            content = { CategoryDetailsScreen(closeScreen = { navController.popBackStack() }) },
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        )
                        composable(
                            route = "CategoryDetailsScreen",
                            content = { CategoryDetailsScreen(closeScreen = { navController.popBackStack() }) },
                        )
                        composable(
                            route = Statistic.route,
                            content = { StatisticScreen(navController) },
                        )
                    }
                }
            }
        }

    }
}
