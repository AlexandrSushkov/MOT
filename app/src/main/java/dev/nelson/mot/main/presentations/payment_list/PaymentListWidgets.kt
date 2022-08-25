@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.payment_list.compose.widgets

import android.animation.TimeInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.IconButton
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.util.compose.PreviewData

@Preview(showBackground = true)
@Composable
fun ToolbarMotPreview() {
    TopAppBarMot("Toolbar")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMot(title: String) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icons.Default.Menu
            }
        },
        title = { Text(text = title) }
    )
}

@Preview(showBackground = true)
@Composable
fun PaymentListDateItemPreview() {
    PaymentListDateItem("01.11.2022")
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

// interactive mode available
@Preview(showBackground = true, backgroundColor = 1)
@Composable
fun DismissiblePaymentListItemPreview() {
    DismissiblePaymentListItem(
        payment = PreviewData.paymentItemPreview,
        onClick = {},
        onSwipeToDelete = {},
        MutableLiveData(true)
    )
}

@Composable
fun DismissiblePaymentListItem(
    payment: Payment,
    onClick: (Payment) -> Unit,
    onSwipeToDelete: (Payment) -> Unit,
    isExpanded: MutableLiveData<Boolean>
) {
    val haptic = LocalHapticFeedback.current
    val expanded by isExpanded.observeAsState(false)

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
                targetValue = if (dismissState.targetValue == DismissValue.Default) 0.7f else 1.1f,
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
                ExpandableArea(payment = payment, onCardArrowClick = {}, expanded = false)
            }
        }
    }
}

@Composable
fun ExpandableArea(
    payment: Payment,
    onCardArrowClick: (Payment) -> Unit,
    expanded: Boolean,
) {
    val EXPAND_ANIMATION_DURATION = 500
    val transitionState = remember { MutableTransitionState(payment.isExpanded) }
    val transition = updateTransition(transitionState, label = "")

    val cardBgColor by transition.animateColor(
//        transitionSpec = { tween(durationMillis = EXPAND_ANIMATION_DURATION) },
        label = "",
        targetValueByState = { if (it) Color.DarkGray else Color.LightGray }
    )
//    val cardPaddingHorizontal by transition.animateDp({
//        tween(durationMillis = EXPAND_ANIMATION_DURATION)
//    }, targetValueByState = {
//        if (it) 48.dp else 24.dp
//    }, label = "")
//    val cardElevation by transition.animateDp({
//        tween(durationMillis = EXPAND_ANIMATION_DURATION)
//    }) {
//        if (it) 24.dp else 4.dp
//    }
//    val cardRoundedCorners by transition.animateDp({
//        tween(
////            durationMillis = EXPAND_ANIMATION_DURATION,
//            easing = FastOutSlowInEasing
//        )
//    }, label = "") {
//        if (transitionState.currentState) 0.dp else 16.dp
//    }
    val arrowRotationDegree by transition.animateFloat(
//        transitionSpec = { tween(durationMillis = EXPAND_ANIMATION_DURATION) },
        label = "expandable arrow",
//        targetValueByState = { if (transition.currentState) 0f else 180f }
        targetValueByState = { if (it) 0f else 180f }
    )

    Column {
        Box(
            modifier = Modifier
                .background(cardBgColor)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    transitionState.targetState = !transitionState.currentState
                    payment.isExpanded = transitionState.currentState
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Expandable Arrow",
                        modifier = Modifier.rotate(arrowRotationDegree),
                    )
                }
            )
        }
        ExpandableContent(text = payment.message, visible = transitionState.currentState, initialVisibility = transition.currentState)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableContent(
    text: String,
    visible: Boolean = true,
    initialVisibility: Boolean = false
) {
//    val EXPANSTION_TRANSITION_DURATION = 0
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
//            animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        ) + fadeIn(
            initialAlpha = 0.3f,
//            animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            // Expand from the top.
            shrinkTowards = Alignment.Top,
//            animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        ) + fadeOut(
            // Fade in with the initial alpha of 0.3f.
//            animationSpec = tween(EXPANSTION_TRANSITION_DURATION)
        )
    }
    val state: MutableTransitionState<Boolean> = remember { MutableTransitionState(initialState = initialVisibility) }
        .apply { targetState = visible }
    AnimatedVisibility(
        visibleState = state,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 1)
@Composable
fun asdf() {
    ExpandableArea(payment = PreviewData.paymentItemPreview, onCardArrowClick = { }, expanded = false)
}
