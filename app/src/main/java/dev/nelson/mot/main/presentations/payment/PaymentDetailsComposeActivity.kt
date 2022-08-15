package dev.nelson.mot.main.presentations.payment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.presentations.payment.widget.PaymentDetailsLayout
import dev.nelson.mot.main.presentations.ui.theme.MotTheme

@AndroidEntryPoint
class PaymentDetailsComposeActivity : ComponentActivity() {

    private val viewModel: PaymentDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotTheme {
                val name by viewModel.paymentName.observeAsState("")
                val cost by viewModel.paymentCost.observeAsState("")
                val message by viewModel.message.observeAsState("")
                PaymentDetailsLayout(
                    name = name,
                    cost = cost,
                    message = message,
                    onNameChange = { viewModel.paymentName.value = it },
                    onCostChange = { viewModel.paymentCost.value = it },
                    onMessageChange = { viewModel.message.value = it },
                    onSaveClick = { viewModel.onSaveClick() }
                )
            }
        }
        initListeners()
    }

    private fun initListeners() {
        viewModel.finishAction.observe(this) { finish() }
    }

    @Preview(showBackground = true)
    @Composable
    fun Preview() {
        PaymentDetailsLayout(
            name = "",
            cost = "",
            message = "",
            onNameChange = {},
            onCostChange = {},
            onMessageChange = {},
            onSaveClick = {}
        )
    }
}



