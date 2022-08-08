package dev.nelson.mot.main.presentations.payment_list.compose.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.util.compose.PreviewData

@Composable
fun ToolbarMot(title: String) {
    TopAppBar(
        title = { Text(text = title) },
    )
}

@Preview(name = "ToolbarMot light mode", showBackground = true)
@Composable
fun ToolbarMotPreview() {
    ToolbarMot("Toolbar Title")
}

@Composable
fun PaymentListDateItem(date: String) {
    Row(modifier = Modifier.fillMaxWidth(1f)) {
        Text(
            text = date,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )
    }
}

@Preview(name = "PaymentListDateItem light mode", showBackground = true)
@Composable
fun PaymentListDateItemPreview() {
    PaymentListDateItem("01.11.2022")
}

@Composable
fun PaymentListItem(payment: Payment) {
    Column() {
        Row(modifier = Modifier.padding(all = 16.dp)) {
            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxWidth(1f)
            ) {
                Text(payment.name)
                Text(payment.category?.name ?: "")
            }
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
            ) {
                Text(payment.cost.toString())
            }
        }
    }
}

@Preview(name = "PaymentListItem light mode", showBackground = true)
@Composable
fun PaymentListItemPreview() {
    PaymentListItem(PreviewData.previewPayment)
}


