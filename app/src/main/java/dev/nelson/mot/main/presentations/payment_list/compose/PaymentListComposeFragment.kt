package dev.nelson.mot.main.presentations.payment_list.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.presentations.payment_list.PaymentListViewModel
import dev.nelson.mot.main.presentations.payment_list.compose.widgets.PaymentListItem
import dev.nelson.mot.main.presentations.payment_list.compose.widgets.ToolbarMot
import dev.nelson.mot.main.util.compose.PreviewData

@AndroidEntryPoint
class PaymentListComposeFragment : BaseFragment() {

    private val viewModel: PaymentListViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val payments by viewModel.paymentList.observeAsState(emptyList())
                PaymentListComposeFragmentLayout(payments)
            }
        }
    }
}

@Composable
fun PaymentListComposeFragmentLayout(paymentList: List<Payment>) {
    Column() {
        ToolbarMot(title = "payments list")
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(paymentList.size) {
                PaymentListItem(paymentList[it])
            }
        }
    }
}

@Preview(name = "PaymentListComposeFragment layout", showBackground = true)
@Composable
fun PaymentListComposeFragmentPreview() {
    PaymentListComposeFragmentLayout(List(20) { PreviewData.previewPayment })
}