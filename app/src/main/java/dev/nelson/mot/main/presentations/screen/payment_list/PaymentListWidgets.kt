@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.presentations.widgets.MotDismissibleListItem
import dev.nelson.mot.main.presentations.widgets.MotExpandableArea
import dev.nelson.mot.main.util.compose.PreviewData

@Composable
fun PaymentListDateItem(date: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = date,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
fun PaymentListItem(
    payment: Payment,
    onClick: (Payment) -> Unit,
    dismissDirection: DismissDirection?
) {
    Card(
        modifier = Modifier.clickable(onClick = { onClick.invoke(payment) }),
        elevation = animateDpAsState(targetValue = if (dismissDirection != null) 4.dp else 0.dp).value,
        shape = RoundedCornerShape(0.dp)
    ) {
        Column() {
            Row(
                modifier = Modifier
                    .padding(all = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.0f)
                        .fillMaxWidth()
                ) {
                    Text(payment.name)
                    payment.category?.name?.let { Text(it) }
                }
                Column(
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                ) {
                    Text(payment.cost.toString())
                }
            }
            if (payment.message.isNotEmpty()) {
                MotExpandableArea(payment = payment)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentListDateItemPreview() {
    PaymentListDateItem("01.11.2022")
}

@Preview(showBackground = true)
@Composable
private fun DismissiblePaymentListItemPreview() {
    MotDismissibleListItem(
        dismissState = DismissState(DismissValue.Default),
        dismissContent = {
            PaymentListItem(
                payment = PreviewData.paymentItemPreview,
                onClick = {},
                dismissDirection = DismissDirection.EndToStart
            )
        }
    )
}

