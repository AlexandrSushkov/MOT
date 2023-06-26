@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotCard
import dev.nelson.mot.core.ui.MotDismissibleListItem
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.PriceText
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.presentations.widgets.MotSingleLineText
import dev.nelson.mot.main.util.compose.MotTransitions
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.capitalizeFirstLetter
import dev.utils.preview.MotPreview

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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 4.dp,
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
            priceViewState = PriceViewState(),
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
    priceViewState: PriceViewState,
    checkBoxTransitionState: MutableTransitionState<Boolean>,
    transition: Transition<Boolean>
) {
    val haptic = LocalHapticFeedback.current
    checkBoxTransitionState.targetState = isSelectedStateOn

    val checkBoxEnterTransition = remember { MotTransitions.enterStartHorizontalTransition }
    val checkBoxExitTransition = remember { MotTransitions.exitStartHorizontalTransition }

    val paymentNamePaddingStart by transition.animateDp(
        transitionSpec = { tween(durationMillis = Constants.DEFAULT_ANIMATION_DURATION) },
        targetValueByState = { if (it) 0.dp else 16.dp },
        label = "paymentNamePaddingStart"
    )

    val cardBackgroundColor = if (paymentItemModel.payment.isSelected) {
        MaterialTheme.colorScheme.tertiaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val cardBackgroundColorState by animateColorAsState(
        targetValue = cardBackgroundColor,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "system_bar_animate_color"
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
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColorState
        ),
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
                        start = paymentNamePaddingStart,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1.0f)
                            .fillMaxWidth()
                    ) {
                        Row {
                            Column(modifier = Modifier.weight(1f)) {
                                MotSingleLineText(
                                    text = paymentItemModel.payment.name.capitalizeFirstLetter(),
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                if (paymentItemModel.showCategory) {
                                    paymentItemModel.payment.category?.name?.let {
                                        MotSingleLineText(
                                            text = it,
                                            style = MaterialTheme.typography.labelMedium,
                                        )
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier.align(alignment = Alignment.CenterVertically)
                            ) {
                                PriceText(
                                    modifier = Modifier.padding(start = 16.dp),
                                    price = paymentItemModel.payment.cost,
                                    priceViewState = priceViewState,
                                )
                            }
                        }
                        if (paymentItemModel.payment.message.isNotEmpty()) {
                            Column {
//                                MotVerticalExpandableArea(payment = paymentItemModel.payment)
                                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                                    Text(
                                        style = MaterialTheme.typography.bodySmall,
                                        text = paymentItemModel.payment.message,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 2,
                                    )
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    ) {
//                        PriceText(
//                            price = paymentItemModel.payment.cost,
//                            priceViewState = priceViewState,
//                        )
                    }

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
                priceViewState = PriceViewState(),
                checkBoxTransitionState = checkBoxTransitionState,
                transition = updateTransition(
                    checkBoxTransitionState,
                    label = "paymentNamePaddingStart"
                )
            )
        })
    }
}
