package dev.nelson.mot.main.presentations.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Just a spacer to add space between the last item in the list and the bottom of a screen
 * to prevent FAB overlapping.
 */
@Composable
fun FABFooter() {
    Spacer(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
//        .background(Color.Blue)
    )
}
