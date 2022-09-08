@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.screen.payment_list.compose.widgets

import android.animation.TimeInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.presentations.widgets.MotExpandableArea
import dev.nelson.mot.main.util.compose.PreviewData


@Composable
fun PaymentListDateItem(date: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = date,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentListDateItemPreview() {
    PaymentListDateItem("01.11.2022")
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MotDismissiblePaymentListItem(
    payment: Payment,
    onClick: (Payment) -> Unit,
    onSwipeToDelete: (Payment) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                onSwipeToDelete.invoke(payment)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            fun TimeInterpolator.toEasing() = Easing { x -> getInterpolation(x) }
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToEnd -> Color.Green
                    DismissValue.DismissedToStart -> Color.Red
                }
            )
            val iconColor by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    DismissValue.Default -> Color.DarkGray
                    DismissValue.DismissedToEnd -> Color.Green
                    DismissValue.DismissedToStart -> Color.White
                },
                finishedListener = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) }
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Done
                DismissDirection.EndToStart -> Icons.Default.Delete
            }
            val scale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.1f,
                animationSpec = FloatTweenSpec(500, 0, AnticipateOvershootInterpolator().toEasing()),
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    tint = iconColor,
                    contentDescription = "Localized description",
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = { PaymentListItem(payment = payment, onClick = onClick, dismissState.dismissDirection) },
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = { FractionalThreshold(0.35f) }
    )
}

// interactive mode available
@Preview(showBackground = true, backgroundColor = 1)
@Composable
private fun DismissiblePaymentListItemPreview() {
    MotDismissiblePaymentListItem(
        payment = PreviewData.paymentItemPreview,
        onClick = {}
    ) {}
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
                MotExpandableArea(
                    payment = payment,
                )
            }
        }
    }
}

