package dev.nelson.mot.main.presentations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel

sealed class MotDestinations(
    val icon: ImageVector,
    val route: String,
//    val vm = ViewModel,
    val screen: @Composable () -> Unit
)

/**
 * Mot app navigation destinations
 */
object PaymentList : MotDestinations(
    icon = Icons.Filled.PieChart,
    route = "PaymentList",
//    vm = hiltViewModel()
    screen = {
//        OverviewScreen()
    }
)

object CategoryList : MotDestinations(
    icon = Icons.Filled.AttachMoney,
    route = "CategoryList",
    screen = {
//     AccountsScreen()
    }
)

object Overview : MotDestinations(
    icon = Icons.Filled.AttachMoney,
    route = "Overview",
    screen = {
//     AccountsScreen()
    }
)

//object Bills : MotDestinations {
//    override val icon = Icons.Filled.MoneyOff
//    override val route = "bills"
//    override val screen: @Composable () -> Unit = { BillsScreen() }
//}
//
//object SingleAccount : RallyDestination {
//    // Added for simplicity, this icon will not in fact be used, as SingleAccount isn't
//    // part of the RallyTabRow selection
//    override val icon = Icons.Filled.Money
//    override val route = "single_account"
//    override val screen: @Composable () -> Unit = { SingleAccountScreen() }
//    const val accountTypeArg = "account_type"
//}

// Screens to be displayed in the top motTabRow
val motTabRowScreens = listOf(Overview, PaymentList, CategoryList)