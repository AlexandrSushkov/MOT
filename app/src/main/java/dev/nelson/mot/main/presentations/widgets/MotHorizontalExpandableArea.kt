package dev.nelson.mot.main.presentations.widgets

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.compose.MotTransitions
import dev.nelson.mot.main.util.constant.Constants

@Composable
fun MotHorizontalExpandableArea(
    payment: Payment,
//    content: @Composable BoxScope.() -> Unit
) {
    val transitionState = remember { MutableTransitionState(payment.isExpanded) }
    val transition = updateTransition(transitionState, label = "")

    val arrowRotationDegree by transition.animateFloat(
        transitionSpec = { tween(durationMillis = Constants.DEFAULT_ANIMATION_DURATION) },
        label = "expandable arrow",
        targetValueByState = { if (it) 0f else 180f }
    )

    val enterTransition = remember { MotTransitions.enterStartHorizontalTransition }
    val exitTransition = remember { MotTransitions.exitStartHorizontalTransition }

    Row(modifier = Modifier.fillMaxWidth()) {
        androidx.compose.animation.AnimatedVisibility(
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

@Preview(showBackground = true)
@Composable
private fun MotExpandableAreaPreview() {
    MotHorizontalExpandableArea(
        payment = PreviewData.paymentItemPreview,
    )
}