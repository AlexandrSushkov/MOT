package dev.nelson.mot.main.presentations.payment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.presentations.payment.ui.theme.MotTheme
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



