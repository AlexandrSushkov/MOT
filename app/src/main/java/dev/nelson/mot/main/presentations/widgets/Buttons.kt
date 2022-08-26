package dev.nelson.mot.main.presentations.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun MotButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    FilledTonalButton(
        modifier = modifier,
        onClick = onClick,
        content = content
    ) 
}

@Preview(showBackground = true)
@Composable
fun MotButtonPreview() {
    MotButton(
        onClick = {},
        modifier = Modifier,
        content = { Text(text = "FilledTonalButton")}
    )
}

@Preview(showBackground = true)
@Composable
fun Buttons(){
    Column {
        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp),
            onClick = {  }
        ) {
            Text(text = "Button")
        }
        ElevatedButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp),
            onClick = {  }
        ) {
            Text(text = "ElevatedButton")
        }
        OutlinedButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp),
            onClick = {  }
        ) {
            Text(text = "OutlinedButton")
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp),
            onClick = {  }
        ) {
            Icon(Icons.Default.Save, contentDescription = "IconButton")
        }
        ExtendedFloatingActionButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp),
            text = { Text(text = "ExtendedFloatingActionButton") },
            onClick = {  }
        )
        FilledTonalButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp),
            onClick = {  }
        ) {
            Text(text = "FilledTonalButton")
        }
        TextButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp),
            onClick = {  }
        ) {
            Text(text = "TextButton")
        }
    }
}
