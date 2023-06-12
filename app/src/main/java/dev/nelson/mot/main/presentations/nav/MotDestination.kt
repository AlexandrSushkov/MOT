package dev.nelson.mot.main.presentations.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tag
import androidx.compose.ui.graphics.vector.ImageVector

interface MotDestination {
    val route: String
    val icon: ImageVector
//    val screen: @Composable () -> Unit
}

object Dashboard : MotDestination {
    override val route = "Dashboard"
    override val icon = Icons.Default.Dashboard
//    override val screen: @Composable () -> Unit = { PaymentListScreen({}, {}) }
}

object Payments : MotDestination {
    override val route = "Payments"
    override val icon = Icons.Default.List
//    override val screen: @Composable () -> Unit = { PaymentListScreen({}, {}) }
}

object PaymentDetails : MotDestination {
    override val route = "PaymentDetails"
    override val icon = Icons.Default.Payments
//    override val screen: @Composable () -> Unit = { PaymentListScreen({}, {}) }
}

object Categories : MotDestination {
    override val route = "Categories"
    override val icon = Icons.Default.Tag
//    override val screen: @Composable () -> Unit = { CategoryListScreen({}, {}, {}) }
}

object CategoryDetails : MotDestination {
    override val route = "CategoryDetails"
    override val icon = Icons.Default.Category
//    override val screen: @Composable () -> Unit = { PaymentListScreen({}, {}) }
}

object Statistic : MotDestination {
    override val route = "Statistic"
    override val icon = Icons.Default.BarChart
//    override val screen: @Composable () -> Unit = { CategoryListScreen({}, {}, {}) }
}

object Settings : MotDestination {
    override val route = "Settings"
    override val icon = Icons.Default.Settings
//    override val screen: @Composable () -> Unit = { CategoryListScreen({}, {}, {}) }
}

object CountryPicker : MotDestination {
    override val route = "CountryPicker"
    override val icon = Icons.Default.Settings
//    override val screen: @Composable () -> Unit = { CategoryListScreen({}, {}, {}) }
}

object AppThemePicker : MotDestination {
    override val route = "AppThemePicker"
    override val icon = Icons.Default.Settings
//    override val screen: @Composable () -> Unit = { CategoryListScreen({}, {}, {}) }
}

val initialDrawerItemsList = listOf(Dashboard, Payments, Categories, Settings)

