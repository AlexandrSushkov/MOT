package dev.nelson.mot.main.presentations.payment_list

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import dev.nelson.mot.main.util.compose.PreviewData

@Preview(showBackground = true)
@Composable
fun PaymentListScreenPreview() {
    PaymentListComposeFragmentLayout(
        paymentList = PreviewData.paymentListPreview,
        onItemClick = {},
        onSwipeToDelete = {},
        isExpanded = MutableLiveData(false)
    )
}

@Composable
fun PaymentListScreen(navigateToCategories: () -> Unit) {
    Box() {
        val viewModel = hiltViewModel<PaymentListViewModel>()

        val payments by viewModel.paymentList.collectAsState(emptyList())
        val isExpanded = viewModel.expandedLiveData
        PaymentListComposeFragmentLayout(
            payments,
            onItemClick = { viewModel.onItemClick(it) },
            onSwipeToDelete = { viewModel.deletePayment(it) },
            isExpanded
        )
//        Button(onClick = navigateToCategories) {
//
//        }
    }
}
