package dev.nelson.mot.main.presentations.payment.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.presentations.ui.theme.MotTheme

@Composable
fun PaymentDetailsLayout(
    name: String,
    cost: String,
    message: String,
    onNameChange: (String) -> Unit,
    onCostChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column() {
            Row {
                TextField(
                    value = if (LocalInspectionMode.current) "preview new payment" else name,
                    onValueChange = onNameChange,
                    placeholder = { Text(text = "new payment") }
                )
                TextField(
                    value = cost,
                    onValueChange = onCostChange,
                    placeholder = { Text(text = "0.0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = message,
                onValueChange = onMessageChange,
                placeholder = { Text(text = "message") },
            )
            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp),
                onClick = onSaveClick
            ) {
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
            message = "",
            onNameChange = {},
            onCostChange = {},
            onMessageChange = {},
            onSaveClick = {}
        )
    }
}