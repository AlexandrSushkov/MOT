package dev.nelson.mot.main.presentations.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun MotButton() {
    Button(
        modifier = Modifier.padding(16.dp),
        onClick = {}
    ) {
        Text(text = "Save")
    }
}

@Preview(showBackground = true)
@Composable
fun MotButtonPreview() {
    MotButton()
}