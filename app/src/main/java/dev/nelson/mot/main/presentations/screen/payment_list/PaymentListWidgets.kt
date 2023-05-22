@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotCard
import dev.nelson.mot.core.ui.MotDismissibleListItem
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.PriceText
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.presentations.widgets.MotVerticalExpandableArea
import dev.nelson.mot.main.util.compose.MotTransitions
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.constant.Constants
import dev.utils.preview.MotPreview
import java.util.Locale

@Composable
fun DateRangeWidget(startDate: String, endDate: String) {
    Column(modifier = Modifier.clickable { }) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            DateRageDateText(
                modifier = Modifier.align(Alignment.CenterVertically),
                date = startDate
            )
            Icon(
                Icons.Default.ArrowRightAlt,
                modifier = Modifier.weight(1f),
                contentDescription = "arrow right"
            ) 
            DateRageDateText(
                modifier = Modifier.align(Alignment.CenterVertically),
                date = endDate
            )
        }
        Divider(
            Modifier
                .height(1.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

@Composable
private fun DateRageDateText(
    modifier: Modifier = Modifier,
    date: String
) {
    Text(
        modifier = modifier,
        text = date,
        style = MaterialTheme.typography.bodySmall
    )
}

@MotPreview
@Composable
private fun DateRangeWidgetPreview() {
    MotMaterialTheme {
        DateRangeWidget(startDate = "11.11.11", endDate = "22.22.22")
    }
}

@Composable
fun PaymentListDateItem(date: String) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
    ) {
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
}

@MotPreview
@Composable
private fun PaymentListDateItemPreview() {
    MotMaterialTheme {
        PaymentListDateItem("01.11.2022")
    }
}

@MotPreview
@Composable
fun PaymentListItemSelectedPreview() {
    val checkBoxTransitionState = remember { MutableTransitionState(true) }
    MotMaterialTheme {
        PaymentListItem(
            paymentItemModel = PreviewData.paymentItemModelPreview,
            onClick = {},
            onLongClick = {},
            dismissDirection = DismissDirection.EndToStart,
            isSelectedStateOn = true,
            locale = Locale.getDefault(),
            showCents = true,
            showCurrencySymbol = true,
            checkBoxTransitionState = checkBoxTransitionState,
            transition = updateTransition(
                checkBoxTransitionState,
                label = "paymentNamePaddingStart"
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaymentListItem(
    paymentItemModel: PaymentListItemModel.PaymentItemModel,
    onClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    onLongClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    dismissDirection: DismissDirection?,
    isSelectedStateOn: Boolean,
    locale: Locale,
    showCents: Boolean,
    showCurrencySymbol: Boolean,
    checkBoxTransitionState: MutableTransitionState<Boolean>,
    transition: Transition<Boolean>
) {
    val haptic = LocalHapticFeedback.current
    checkBoxTransitionState.targetState = isSelectedStateOn

    val checkBoxEnterTransition = remember { MotTransitions.enterStartHorizontalTransition }
    val checkBoxExitTransition = remember { MotTransitions.exitStartHorizontalTransition }

    val paymentNamePaddingStart by transition.animateDp(
        transitionSpec = { tween(durationMillis = Constants.DEFAULT_ANIMATION_DURATION) },
        targetValueByState = { if (it) 0.dp else 24.dp },
        label = "paymentNamePaddingStart"
    )

    MotCard(
        modifier = Modifier.combinedClickable(
            onClick = { onClick.invoke(paymentItemModel) },
            onLongClick = {
                if (isSelectedStateOn.not()) {
                    onLongClick.invoke(paymentItemModel)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }

            }),
//        elevation = animateDpAsState(targetValue = if (dismissDirection != null) 4.dp else 0.dp).value,
    ) {
        Row {
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                AnimatedVisibility(
                    visibleState = checkBoxTransitionState,
                    enter = checkBoxEnterTransition,
                    exit = checkBoxExitTransition,
                ) {
                    Checkbox(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        checked = paymentItemModel.payment.isSelected,
                        onCheckedChange = { onClick.invoke(paymentItemModel) }
                    )
                }
            }
            Column {
                Row(
                    modifier = Modifier.padding(
//                        start = if (isSelectedStateOn) 0.dp else 24.dp,
                        start = paymentNamePaddingStart,
                        end = 24.dp,
                        top = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.0f)
                            .fillMaxWidth()
                    ) {
                        Text(text = paymentItemModel.payment.name)
                        if (paymentItemModel.showCategory) {
                            paymentItemModel.payment.category?.name?.let { Text(it) }
                        }
                    }
                    Column(
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    ) {
                        PriceText(
                            locale = locale,
                            isShowCents = showCents,
                            priceInCents = paymentItemModel.payment.cost,
                            isShowCurrencySymbol = showCurrencySymbol
                        )
                    }
                }
                if (paymentItemModel.payment.message.isNotEmpty()) {
                    MotVerticalExpandableArea(payment = paymentItemModel.payment)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@MotPreview
@Composable
private fun DismissiblePaymentListItemPreview() {
    val checkBoxTransitionState = remember { MutableTransitionState(false) }
    MotMaterialTheme {
        MotDismissibleListItem(dismissState = DismissState(DismissValue.Default), dismissContent = {
            PaymentListItem(
                paymentItemModel = PreviewData.paymentItemModelPreview,
                onClick = {},
                onLongClick = {},
                dismissDirection = DismissDirection.EndToStart,
                isSelectedStateOn = false,
                locale = Locale.getDefault(),
                showCents = true,
                showCurrencySymbol = true,
                checkBoxTransitionState = checkBoxTransitionState,
                transition = updateTransition(
                    checkBoxTransitionState,
                    label = "paymentNamePaddingStart"
                )
            )
        })
    }
}
