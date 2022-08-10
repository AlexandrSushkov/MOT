package dev.nelson.mot.main.presentations.payment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.presentations.ui.theme.MotTheme
import dev.nelson.mot.main.presentations.payment.widget.PaymentDetailsLayout

@AndroidEntryPoint
class PaymentDetailsComposeActivity : ComponentActivity() {

    private val viewModel: PaymentDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotTheme {
                Layout()
            }
        }
        initListeners()
    }

    @Composable
    fun Layout() {
        val name by viewModel.paymentName.observeAsState("")
        val cost by viewModel.paymentCost.observeAsState("")
        PaymentDetailsLayout(
            name = name,
            cost = cost,
            onNameChange = { viewModel.paymentName.value = it },
            onCostChange = { viewModel.paymentCost.value = it },
            onSaveClick = { viewModel.onSaveClick() }
        )
    }

    private fun initListeners() {
        viewModel.finishAction.observe(this) { finish() }
    }

}



