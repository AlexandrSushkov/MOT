package dev.nelson.mot.main.presentations.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.presentations.categories_list.CategoryListScreen
import dev.nelson.mot.main.presentations.category_details.CategoryDetailsScreen
import dev.nelson.mot.main.presentations.payment.PaymentDetailsScreen
import dev.nelson.mot.main.presentations.payment_list.PaymentListScreen
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

@Composable
fun App() {
    MotTheme {

        val navController = rememberNavController()
        val drawerValue = if (LocalInspectionMode.current) DrawerValue.Open else DrawerValue.Closed
        val drawerState = rememberDrawerState(drawerValue)
        val scope = rememberCoroutineScope()

        fun customShape() = object : Shape {
            override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
                return Outline.Rectangle(Rect(0f, 0f, 100f /* width */, 131f /* height */))
            }
        }

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
                        Column(modifier = Modifier.fillMaxHeight()) {
                            Button(onClick = {
                                navController.navigate("PaymentListScreen")
                                scope.launch { drawerState.close() }
                            }) {
                                Text(text = "PaymentListScreen")
                            }
                            Button(onClick = {
                                navController.navigate("CategoryListScreen")
                                scope.launch { drawerState.close() }
                            }) {
                                Text(text = "CategoryListScreen")
                            }
                        }
                    },
//                    drawerShape = customShape()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "CategoryListScreen"
                    ) {
                        composable(
                            route = "PaymentListScreen",
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
                            route = "CategoryListScreen",
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
                    }
                }
            }
        }

    }
}
