package dev.nelson.mot.main.presentations.widgets

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.AppIcons

private const val EXPAND_ANIMATION_DURATION = 500
private const val EXPAND_TRANSITION_DURATION = 300

@Composable
fun MotExpandableTextField(
    isExpanded: Boolean
) {
    val transitionState = remember { MutableTransitionState(isExpanded) }
    val transition = updateTransition(transitionState, label = "")

    val cardBgColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = EXPAND_ANIMATION_DURATION) },
        targetValueByState = { if (it) Color.DarkGray else Color.LightGray },
        label = ""
    )

    val underlineColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = EXPAND_ANIMATION_DURATION) },
        targetValueByState = { if (it) Color.Transparent else Color.LightGray },
        label = ""
    )

    val cardPaddingHorizontal by transition.animateDp(
        transitionSpec = { tween(durationMillis = EXPAND_ANIMATION_DURATION) },
        targetValueByState = { if (it) 0.dp else 24.dp },
        label = ""
    )

    val cardPaddingVertical by transition.animateDp(
        transitionSpec = { tween(durationMillis = EXPAND_ANIMATION_DURATION) },
        targetValueByState = { if (it) 0.dp else 8.dp },
        label = ""
    )
//
//    val cardElevation by transition.animateDp(
//        transitionSpec = { tween(durationMillis = EXPAND_ANIMATION_DURATION) },
//        targetValueByState = { if (it) 24.dp else 4.dp },
//        label = ""
//    )
    val cardRoundedCorners by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = EXPAND_ANIMATION_DURATION,
                easing = FastOutSlowInEasing
            )
        },
        targetValueByState = { if (it) 0.dp else 32.dp },
        label = ""
    )

    val arrowRotationDegree by transition.animateFloat(
        transitionSpec = { tween(durationMillis = EXPAND_ANIMATION_DURATION) },
        targetValueByState = { if (it) 0f else 180f },
        label = "expandable arrow"
    )

    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(EXPAND_TRANSITION_DURATION)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(EXPAND_TRANSITION_DURATION)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            // Expand from the top.
            shrinkTowards = Alignment.Top,
            animationSpec = tween(EXPAND_TRANSITION_DURATION)
        ) + fadeOut(
//             Fade in with the initial alpha of 0.3f.
            animationSpec = tween(EXPAND_TRANSITION_DURATION)
        )
    }
    val state = remember { MutableTransitionState(initialState = transition.currentState) }
        .apply { targetState = transitionState.currentState }

    Column {
        Box(
            modifier = Modifier
//                .background(
//                    color = cardBgColor,
//                    shape = RoundedCornerShape(cardRoundedCorners)
//                )
                .padding(cardPaddingHorizontal, cardPaddingVertical)
                .fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                leadingIcon = { AppIcons.Search() },
                placeholder = { Text(text = "Search") },
                shape = RoundedCornerShape(cardRoundedCorners),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = { transitionState.targetState = !transitionState.currentState },
                content = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Expandable Arrow",
                        modifier = Modifier.rotate(arrowRotationDegree)
                    )
                }
            )
        }
//        AnimatedVisibility(
//            visibleState = state,
//            enter = enterTransition,
//            exit = exitTransition,
//        ) {
//            Text(
//                text = "Hello World",
//                modifier = Modifier.padding(horizontal = 16.dp),
//            )
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun MotExpandableTextFieldPreview() {
    MotExpandableTextField(
        isExpanded = false
    )
}
