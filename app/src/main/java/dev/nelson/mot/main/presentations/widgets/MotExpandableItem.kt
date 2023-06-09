package dev.nelson.mot.main.presentations.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.constant.Constants
import dev.utils.preview.MotPreview

@Composable
fun MotExpandableItem(
    titleContent: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
    expandButtonIcon: ImageVector,
    onExpandButtonClick: () -> Unit = {},
    expandedState: MutableTransitionState<Boolean>
) {
    val transition = updateTransition(expandedState, label = "")

    val arrowRotationDegree by transition.animateFloat(
        transitionSpec = { tween(durationMillis = Constants.DEFAULT_ANIMATION_DURATION) },
        label = "expandable icon",
        targetValueByState = { if (it) 0f else 180f }
    )

    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION),
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION),
        )
    }

    Surface {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                titleContent()
                IconButton(
                    onClick = onExpandButtonClick,
                    content = {
                        Icon(
                            imageVector = expandButtonIcon,
                            contentDescription = "Expandable Arrow",
                            modifier = Modifier.rotate(arrowRotationDegree),
                        )
                    }
                )
            }
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red),
                visibleState = expandedState,
                enter = enterTransition,
                exit = exitTransition,
                content = { expandedContent() }
            )
        }
    }
}

@MotPreview
@Composable
private fun MotExpandableItemPreview() {
    MotMaterialTheme {
        val payment = PreviewData.paymentItemPreview
        val expandedState = remember { MutableTransitionState(payment.isExpanded) }

        MotExpandableItem(
            expandedState = expandedState,
            titleContent = { Text(text = payment.name) },
            expandedContent = {
                Text(
                    text = payment.message,
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            expandButtonIcon = Icons.Default.KeyboardArrowUp,
            onExpandButtonClick = {
                payment.isExpanded = !expandedState.currentState
                expandedState.targetState = payment.isExpanded
            }
        )
    }
}
