package dev.nelson.mot.main.presentations.screen.statistic

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.presentations.motTabRowScreens
import dev.nelson.mot.core.ui.LineChartMot
import dev.nelson.mot.core.ui.PriceText
import java.util.Locale

@Composable
fun StatisticScreen(
    viewModel: StatisticViewModel,
    navHostController: NavHostController
) {
    val currentMonth by viewModel.currentMonthListResult.collectAsState(emptyMap())
    val previousMonthList by viewModel.previousMonthListResult.collectAsState(emptyMap())
    val showCents by viewModel.showCents.collectAsState(false)
    val showCurrencySymbol by viewModel.showCurrencySymbol.collectAsState(false)
    val selectedLocale by viewModel.selectedLocale.collectAsState(Locale.getDefault())

    StatisticLayout(
        navHostController,
        currentMonth,
        previousMonthList,
        showCents,
        showCurrencySymbol,
        selectedLocale
    )

}

@Composable
fun StatisticLayout(
    navHostController: NavHostController,
    currentMonthList: Map<Category?, List<PaymentListItemModel.PaymentItemModel>>,
    previousMonthList: Map<Category?, List<PaymentListItemModel.PaymentItemModel>>,
    showCents: Boolean,
    showCurrencySymbol: Boolean,
    selectedLocale: Locale
) {
    Scaffold(
        bottomBar = {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                BottomNavigation {
                    motTabRowScreens.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                            label = { Text(screen.route) },
                            selected = navHostController.currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {}
                        )
                    }
                }
            }
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            ) {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Row {
                    NavigationRail {
                        motTabRowScreens.forEach { screen ->
                            NavigationRailItem(
                                label = { Text(screen.route) },
                                icon = { Icon(screen.icon, contentDescription = "") },
                                selected = navHostController.currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = { }
                            )
                        }
                    }
                    Column {
                        StatisticContent()
                    }
                }
            } else {
                StatisticContent()
                val paymentsForCurrentMonth = mutableListOf<PaymentListItemModel.PaymentItemModel>()
                val paymentsForPreviousMonth =
                    mutableListOf<PaymentListItemModel.PaymentItemModel>()
                currentMonthList.entries.forEach {
                    paymentsForCurrentMonth.addAll(it.value)
                }
                previousMonthList.entries.forEach {
                    paymentsForPreviousMonth.addAll(it.value)
                }
                val sumForCurrentMonth = paymentsForCurrentMonth.sumOf { it.payment.cost }
                val sumForPreviousMonth = paymentsForPreviousMonth.sumOf { it.payment.cost }
                Row {
                    Text(
                        text = "Current month total: ",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    PriceText(
                        locale = selectedLocale,
                        isShowCents = showCents,
                        priceInCents = sumForCurrentMonth,
                        isShowCurrencySymbol = showCurrencySymbol
                    )
                }
                Divider()
                Text(
                    text = "Current month:",
                    style = MaterialTheme.typography.labelLarge,
                )
                LazyColumn(content = {
                    currentMonthList.keys.forEach {
                        item {
                            Row {
                                Text(
                                    text = it?.name?.let { name -> "$name:" } ?: "NO category:",
                                    style = MaterialTheme.typography.labelLarge,
                                )
                                PriceText(
                                    locale = selectedLocale,
                                    isShowCents = showCents,
                                    priceInCents = currentMonthList[it].let { pl ->
                                        pl?.sumOf { payment -> payment.payment.cost } ?: 0
                                    },
                                    isShowCurrencySymbol = showCurrencySymbol
                                )
                            }

                        }
                    }
                })
                Divider()
                Row {
                    Text(
                        text = "Previous month total: ",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    PriceText(
                        locale = selectedLocale,
                        isShowCents = showCents,
                        priceInCents = sumForPreviousMonth,
                        isShowCurrencySymbol = showCurrencySymbol
                    )
                }
                Divider()
                Text(
                    text = "Previous month by categories:",
                    style = MaterialTheme.typography.labelLarge,
                )
                LazyColumn(content = {
                    previousMonthList.keys.forEach {
                        item {
                            Row {
                                Text(
                                    text = it?.name?.let { name -> "$name:" } ?: "NO category:",
                                    style = MaterialTheme.typography.labelLarge,
                                )
                                PriceText(
                                    locale = selectedLocale,
                                    isShowCents = showCents,
                                    priceInCents = previousMonthList[it].let { pl ->
                                        pl?.sumOf { paymentModel -> paymentModel.payment.cost } ?: 0
                                    },
                                    isShowCurrencySymbol = showCurrencySymbol
                                )
                            }

                        }
                    }
                })
            }
        }
    }
}

@Composable
fun StatisticContent() {
    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
        LineChartMot(items = listOf(0.2f, 0.5f, 0.1f, 0.3f))
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticLayoutLightPreview() {
    StatisticLayout(
        NavHostController(LocalContext.current),
        currentMonthList = emptyMap(),
        previousMonthList = emptyMap(),
        showCents = true,
        showCurrencySymbol = true,
        selectedLocale = Locale.getDefault()
    )
}

@Preview(showBackground = true)
@Composable
private fun StatisticLayoutDarkPreview() {
    MotMaterialTheme(darkTheme = true) {
        StatisticLayout(
            NavHostController(LocalContext.current),
            currentMonthList = emptyMap(),
            previousMonthList = emptyMap(),
            showCents = true,
            showCurrencySymbol = true,
            selectedLocale = Locale.getDefault()
        )
    }
}
