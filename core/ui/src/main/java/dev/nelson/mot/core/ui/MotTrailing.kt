package dev.nelson.mot.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MotTrailing(
    content: @Composable () -> Unit
) {
    Row {
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .height(24.dp)
                .width(1.dp)
                .background(color = DividerDefaults.color)
        )
        Spacer(modifier = Modifier.width(24.dp))
        content()
    }
}