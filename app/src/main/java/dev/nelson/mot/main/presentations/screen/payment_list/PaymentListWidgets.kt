@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Chip
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
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
fun DateRangeWidget(startDate: String, endDate: String) {
    Column(modifier = Modifier.clickable { }) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = startDate, modifier = Modifier.align(Alignment.Start))
            }
            Icon(Icons.Default.ArrowRightAlt, contentDescription = "arrow right")
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = endDate, modifier = Modifier.align(Alignment.End))
            }
        }
        Divider(Modifier.height(1.dp))
    }
}

@Composable
fun PaymentListDateItem(date: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
//        Card(
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .padding(vertical = 6.dp),
//                elevation = 4.dp,
//                shape = RoundedCornerShape(20.dp)
//        ) {
//            Text(
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .padding(vertical = 4.dp, horizontal = 12.dp),
//                text = date,
//                style = MaterialTheme.typography.body2
//            )
//        }

        Chip(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {}
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.caption
            )
        }

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
            Row(modifier = Modifier.padding(all = 16.dp)) {
                Column(
                    modifier = Modifier
                        .weight(1.0f)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = payment.name,
                        style = MaterialTheme.typography.subtitle1,
                    )
                    payment.category?.name?.let { Text(it, style = MaterialTheme.typography.subtitle2) }
                    payment.date?.let { Text(it, style = MaterialTheme.typography.caption) }
                }
                Column(
                    modifier = Modifier.align(alignment = CenterVertically)
                ) {
                    Text(
                        payment.cost.toString(),
                        style = MaterialTheme.typography.subtitle2
                    )
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
private fun DateRangeWidgetPreview() {
    DateRangeWidget(startDate = "11.11.11", endDate = "22.22.22")
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

