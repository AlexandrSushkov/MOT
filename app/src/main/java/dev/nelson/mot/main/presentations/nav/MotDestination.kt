package dev.nelson.mot.main.presentations.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tag
import androidx.compose.ui.graphics.vector.ImageVector

interface MotDestination {
    val icon: ImageVector
    val route: String
//    val screen: @Composable () -> Unit
}

object Payments : MotDestination {
    override val icon = Icons.Default.List
    override val route = "Payments"
//    override val screen: @Composable () -> Unit = { PaymentListScreen({}, {}) }
}

object PaymentDetails : MotDestination {
    override val icon = Icons.Default.Payments
    override val route = "PaymentDetails"
//    override val screen: @Composable () -> Unit = { PaymentListScreen({}, {}) }
}

object Categories : MotDestination {
    override val icon = Icons.Default.Tag
    override val route = "Categories"
//    override val screen: @Composable () -> Unit = { CategoryListScreen({}, {}, {}) }
}

object CategoryDetails : MotDestination {
    override val icon = Icons.Default.Category
    override val route = "CategoryDetails"
//    override val screen: @Composable () -> Unit = { PaymentListScreen({}, {}) }
}

object Statistic : MotDestination {
    override val icon = Icons.Default.BarChart
    override val route = "Statistic"
//    override val screen: @Composable () -> Unit = { CategoryListScreen({}, {}, {}) }
}

object Settings : MotDestination {
    override val icon = Icons.Default.Settings
    override val route = "Settings"
//    override val screen: @Composable () -> Unit = { CategoryListScreen({}, {}, {}) }
}

object CountryPicker : MotDestination {
    override val icon = Icons.Default.Settings
    override val route = "CountryPicker"
//    override val screen: @Composable () -> Unit = { CategoryListScreen({}, {}, {}) }
}

val initialDrawerItemsList = listOf(Payments, Categories, Settings)

