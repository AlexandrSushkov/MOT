@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.payment_list.compose.widgets

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
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    ToolbarMot("Toolbar")
}

@Composable
fun PaymentListDateItem(date: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
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
fun DismissiblePaymentListItem(
    payment: Payment,
    onClick: (Payment) -> Unit,
    onSwipeToDelete: (Payment) -> Unit
) {
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
            //            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
////            if (dismissState.dismissDirection == DismissDirection.EndToStart) {
////                MoveToBinDismissibleBackground()
////            }
//
//            val color by animateColorAsState(targetValue = if (dismissState.targetValue == DismissValue.DismissedToStart) Color.Red else Color.Gray)
//            val icon = Icons.Default.Delete
//            val scale by animateFloatAsState(targetValue = if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f)
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(color)
//                    .padding(12.dp)
//            ) {
//                Icon(
//                    icon,
//                    contentDescription = "delete icon",
//                    modifier = Modifier
//                        .scale(scale)
//                        .align(Alignment.CenterEnd)
//                )
//            }
            fun TimeInterpolator.toEasing() = Easing { x -> getInterpolation(x) }

            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToEnd -> Color.Green
                    DismissValue.DismissedToStart -> Color.Red
                }
            )
            val iconColor by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.DarkGray
                    DismissValue.DismissedToEnd -> Color.Green
                    DismissValue.DismissedToStart -> Color.White
                }
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
                targetValue = if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f,
                animationSpec = FloatTweenSpec(500, 0, AnticipateOvershootInterpolator().toEasing())
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

@Composable
fun MoveToBinDismissibleBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
            .padding(8.dp),
        content = {
            Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    )
}

@Composable
fun MoveToArchiveDismissibleBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
            .padding(8.dp),
        content = {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Move to Archive", fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }

        }
    )
}

//@Preview(name = "MoveToArchiveDismissibleBackgroundPreview", showBackground = true)
//@Composable
//fun MoveToArchiveDismissibleBackgroundPreview() {
//    MoveToArchiveDismissibleBackground()
//}

@Composable
fun PaymentListItem(payment: Payment, onClick: (Payment) -> Unit, dismissDirection: DismissDirection?) {
    Card(
        modifier = Modifier
            .background(Color.White)
            .clickable { onClick.invoke(payment) },
        elevation = animateDpAsState(targetValue = if (dismissDirection != null) 4.dp else 0.dp).value,

    ) {
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
    }

}

@Preview(name = "PaymentListItem light mode", showBackground = true)
@Composable
fun PaymentListItemPreview() {
    PaymentListItem(
        PreviewData.previewPayment,
        onClick = {},
        null
    )
}


