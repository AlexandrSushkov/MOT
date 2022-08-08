package dev.nelson.mot.main.presentations.payment.widget

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import dev.nelson.mot.main.R
import dev.nelson.mot.main.presentations.payment.ui.theme.MotTheme

@Composable
fun PaymentDetailsLayout(
    name: String,
    cost: String,
    onNameChange: (String) -> Unit,
    onCostChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column() {
            Row {
                Image(painter = painterResource(id = R.drawable.ic_info_outline_black_24dp), contentDescription = "payment name icon")
                TextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text(text = "new payment") }
                )
                TextField(
                    value = cost,
                    onValueChange = onCostChange,
                    placeholder = { Text(text = "0.0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Button(onClick = onSaveClick) {
                Text(text = "Save")
            }
        }
    }
}

@Preview(name = "PaymentDetailsLayout light mode", showBackground = true)
//@Preview(name = "dark mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PaymentDetailsLayoutPreview() {
    MotTheme {
        PaymentDetailsLayout(
            name = "new payment",
            cost = "0.0",
            onNameChange = {},
            onCostChange = {},
            onSaveClick = {}
        )
    }
}