package dev.nelson.mot.main.presentations.screen.category_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotTextField
import dev.nelson.mot.main.util.constant.Constants
import dev.utils.preview.MotPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CategoryDetailsScreen(
    viewModel: CategoryDetailsViewModel,
    closeScreen: () -> Unit
) {
    Scaffold { innerPadding ->

        LaunchedEffect(
            key1 = Unit,
            block = { viewModel.closeScreenAction.collect { closeScreen.invoke() } }
        )

        CategoryDetailsLayout(
            innerPadding = innerPadding,
            nameState = viewModel.categoryNameState,
            onNameChanged = { viewModel.onNameChanged(it) },
            onSaveClick = { viewModel.onSaveClick() }
        )
    }
}

@Composable
fun CategoryDetailsLayout(
    innerPadding: PaddingValues,
    nameState: StateFlow<TextFieldValue>,
    onNameChanged: (TextFieldValue) -> Unit,
    onSaveClick: () -> Unit
) {
    val categoryNameValueState by nameState.collectAsState()
    val categoryNameFocusRequester = remember { FocusRequester() }

    LaunchedEffect(
        key1 = Unit,
        block = {
            delay(Constants.DEFAULT_ANIMATION_DELAY)
            categoryNameFocusRequester.requestFocus()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        MotTextField(
            value = categoryNameValueState,
            onValueChange = { onNameChanged.invoke(it) },
            placeholder = { Text(text = "Category name") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(categoryNameFocusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { onSaveClick.invoke() })
        )
        FilledTonalButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(6.dp),
            onClick = { onSaveClick.invoke() }
        ) {
            Text(text = "Save")
        }
    }
}

@MotPreview
@Composable
private fun CategoryDetailsLayoutPreview() {
    MotMaterialTheme {
        CategoryDetailsLayout(
            innerPadding = PaddingValues(),
            nameState = MutableStateFlow(TextFieldValue()),
            onNameChanged = {},
            onSaveClick = {}
        )
    }
}
