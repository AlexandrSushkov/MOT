@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.presentations.ui.theme.MotTheme
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
                Text(
                    text = startDate,
                    modifier = Modifier.align(Alignment.Start),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Icon(Icons.Default.ArrowRightAlt, contentDescription = "arrow right")
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically)
            ) {
                Text(
                    text = endDate,
                    modifier = Modifier.align(Alignment.End),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Divider(
            Modifier
                .height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DateRangeWidgetLightPreview() {
    DateRangeWidget(startDate = "11.11.11", endDate = "22.22.22")
}

@Preview(showBackground = true)
@Composable
private fun DateRangeWidgetDarkPreview() {
    MotTheme(darkTheme = true) {
        DateRangeWidget(startDate = "11.11.11", endDate = "22.22.22")
    }
}

@Composable
fun PaymentListDateItem(date: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = date,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentListDateItemLightPreview() {
    PaymentListDateItem("01.11.2022")
}

@Preview(showBackground = true)
@Composable
private fun PaymentListDateItemDarkPreview() {
    MotTheme(darkTheme = true) {
        PaymentListDateItem("01.11.2022")
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

            }),
        backgroundColor = MaterialTheme.colorScheme.surface,
        elevation = animateDpAsState(targetValue = if (dismissDirection != null) 4.dp else 0.dp).value,
        shape = RoundedCornerShape(0.dp)
    ) {
        Row {
            if (isSelectedState) {
                Column(modifier = Modifier.align(CenterVertically)) {
                    Checkbox(
                        checked = paymentItemModel.payment.isSelected,
                        // adjuster to work with MaterialTheme3
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.secondary,
                            uncheckedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            checkmarkColor = MaterialTheme.colorScheme.surface,
                            disabledColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled),
                        ),
                        onCheckedChange = { onClick.invoke(paymentItemModel) }
                    )
                }
            }
            Column {
                Row(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Column(
                        modifier = Modifier
                            .weight(1.0f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = paymentItemModel.payment.name,
                        )
                        if (paymentItemModel.showCategory) {
                            paymentItemModel.payment.category?.name?.let { Text(it) }
                        }
//                        paymentItemModel.payment.date?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                    }
                    Column(
                        modifier = Modifier.align(alignment = CenterVertically)
                    ) {
                        Text(
                            paymentItemModel.payment.cost.toString(),
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
private fun DismissiblePaymentListItemLightPreview() {
    MotDismissibleListItem(dismissState = DismissState(DismissValue.Default), dismissContent = {
        PaymentListItem(
            paymentItemModel = PreviewData.paymentItemModelPreview,
            onClick = {},
            onLongClick = {},
            dismissDirection = DismissDirection.EndToStart,
            isSelectedState = false
        )
    })
}

@Preview(showBackground = true)
@Composable
private fun DismissiblePaymentListItemDarkPreview() {
    MotTheme(darkTheme = true) {
        MotDismissibleListItem(dismissState = DismissState(DismissValue.Default), dismissContent = {
            PaymentListItem(
                paymentItemModel = PreviewData.paymentItemModelPreview,
                onClick = {},
                onLongClick = {},
                dismissDirection = DismissDirection.EndToStart,
                isSelectedState = false
            )
        })
    }
}

