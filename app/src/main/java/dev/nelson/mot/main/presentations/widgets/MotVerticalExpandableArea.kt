package dev.nelson.mot.main.presentations.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.util.compose.MotTransitions
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.constant.Constants

@Composable
fun MotVerticalExpandableArea(
    payment: Payment,
//    content: @Composable BoxScope.() -> Unit
) {
//    val EXPAND_ANIMATION_DURATION = 500
    val transitionState = remember { MutableTransitionState(payment.isExpanded) }
    val transition = updateTransition(transitionState, label = "")

    val cardBgColor by transition.animateColor(
//        transitionSpec = { tween(durationMillis = EXPAND_ANIMATION_DURATION) },
        label = "",
        targetValueByState = { if (it) Color.DarkGray else Color.LightGray }
    )
    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = Constants.DEFAULT_ANIMATION_DURATION)
    }, targetValueByState = {
        if (it) 48.dp else 24.dp
    }, label = "")
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
        transitionSpec = { tween(durationMillis = Constants.DEFAULT_ANIMATION_DURATION) },
        label = "expandable arrow",
        targetValueByState = { if (it) 0f else 180f }
    )

    val enterTransition = remember { MotTransitions.enterTopVerticalTransition }
    val exitTransition = remember { MotTransitions.exitTopVerticalTransition }

    Column {
        AnimatedVisibility(
            visibleState = transitionState,
            enter = enterTransition,
            exit = exitTransition,
        ) {
            Text(
                text = payment.message,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.End)
                .fillMaxWidth(),
            onClick = {
                payment.isExpanded = !transitionState.currentState
                transitionState.targetState = payment.isExpanded
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
}

@Composable
fun ExpandableContent(
    text: String,
    transitionState: MutableTransitionState<Boolean>,
    content: @Composable () -> Unit
) {
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            // Expand from the top.
            shrinkTowards = Alignment.Top,
            animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
        ) + fadeOut(
            // Fade in with the initial alpha of 0.3f.
            animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
        )
    }
    AnimatedVisibility(
        visibleState = transitionState,
        enter = enterTransition,
        exit = exitTransition,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MotExpandableAreaPreview() {
    MotVerticalExpandableArea(
        payment = PreviewData.paymentItemPreview,
//        content = { Text(text = "data")}
    )
}
