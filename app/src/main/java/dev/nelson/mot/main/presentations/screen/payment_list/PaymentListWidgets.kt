@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.data.model.PaymentListItemModel
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
                    .align(CenterVertically)
            ) {
                Text(text = startDate, modifier = Modifier.align(Alignment.Start))
            }
            Icon(Icons.Default.ArrowRightAlt, contentDescription = "arrow right")
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically)
            ) {
                Text(text = endDate, modifier = Modifier.align(Alignment.End))
            }
        }
        Divider(Modifier.height(1.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaymentListItem(
    paymentItemModel: PaymentListItemModel.PaymentItemModel,
    onClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    onLongClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    dismissDirection: DismissDirection?,
    isSelectedState: Boolean
) {
    val haptic = LocalHapticFeedback.current

    Card(
        modifier = Modifier.combinedClickable(
            onClick = { onClick.invoke(paymentItemModel) },
            onLongClick = {
                if (isSelectedState.not()) {
                    onLongClick.invoke(paymentItemModel)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }

            }
        ),
        elevation = animateDpAsState(targetValue = if (dismissDirection != null) 4.dp else 0.dp).value,
        shape = RoundedCornerShape(0.dp)
    ) {
        Row() {
            if (isSelectedState) {
                Column(modifier = Modifier.align(CenterVertically)) {
                    Checkbox(
                        checked = paymentItemModel.payment.isSelected,
                        onCheckedChange = { onClick.invoke(paymentItemModel) }
                    )
                }
            }
            Column() {
                Row(modifier = Modifier.padding(all = 16.dp)) {
                    Column(
                        modifier = Modifier
                            .weight(1.0f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = paymentItemModel.payment.name,
                            style = MaterialTheme.typography.subtitle1,
                        )
                        if (paymentItemModel.shotCategory) {
                            paymentItemModel.payment.category?.name?.let { Text(it, style = MaterialTheme.typography.subtitle2) }
                        }
//                        paymentItemModel.payment.date?.let { Text(it, style = MaterialTheme.typography.caption) }
                    }
                    Column(
                        modifier = Modifier.align(alignment = CenterVertically)
                    ) {
                        Text(
                            paymentItemModel.payment.cost.toString(),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
                if (paymentItemModel.payment.message.isNotEmpty()) {
                    MotExpandableArea(payment = paymentItemModel.payment)
                }
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
                paymentItemModel = PreviewData.paymentItemModelPreview,
                onClick = {},
                onLongClick = {},
                dismissDirection = DismissDirection.EndToStart,
                isSelectedState = false
            )
        }
    )
}

