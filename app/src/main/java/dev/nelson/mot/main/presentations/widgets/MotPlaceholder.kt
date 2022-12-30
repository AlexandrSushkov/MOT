package dev.nelson.mot.main.presentations.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ListPlaceholder(modifier: Modifier, imageVector: ImageVector, text: String) {
    Column(
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "empty list icon",
            modifier = Modifier
                .size(42.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = text
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListPlaceholderPreview() {
    ListPlaceholder(
        modifier = Modifier.size(24.dp),
        imageVector = Icons.Default.Abc,
        text = "placeholder"
    )
}