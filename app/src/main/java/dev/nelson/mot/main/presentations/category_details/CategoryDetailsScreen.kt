package dev.nelson.mot.main.presentations.category_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.util.Constants
import dev.nelson.mot.main.util.compose.PreviewData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun CategoryDetailsScreen(closeScreen: () -> Unit) {
    Scaffold() { innerPadding ->
        val lifecycleOwner = LocalLifecycleOwner.current
        val viewModel = hiltViewModel<CategoryDetailsViewModel>().apply {
            closeScreenAction.observe(lifecycleOwner) { closeScreen.invoke() }
        }
        CategoryDetailsLayout(
            innerPadding = innerPadding,
            categoryState = viewModel.categoryState
        ) { viewModel.onSaveClick(it) }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryDetailsLayoutPreview() {
    CategoryDetailsLayout(
        innerPadding = PaddingValues(),
        categoryState = flow { emit(PreviewData.categoryPreview) },
        onSaveClick = {}
    )
}

@Composable
fun CategoryDetailsLayout(
    innerPadding: PaddingValues,
    categoryState: Flow<Category>,
    onSaveClick: (String) -> Unit
) {
    val category: Category by categoryState.collectAsState(initial = Category.empty())
    var categoryNameValueState by remember { mutableStateOf(TextFieldValue(text = category.name, selection = TextRange(category.name.length))) }
    val categoryNameFocusRequester = remember { FocusRequester.Default }

    LaunchedEffect(
        key1 = Unit,
        block = {
            delay(Constants.DEFAULT_ANIMATION_DELAY)
            categoryNameFocusRequester.requestFocus()
            if (category.name.isNotEmpty()) {
                categoryNameValueState = TextFieldValue(text = category.name, selection = TextRange(category.name.length))
            }
        })
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
    ) {
        TextField(
            value = categoryNameValueState,
            onValueChange = { categoryNameValueState = it },
            placeholder = { Text(text = "new payment") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(categoryNameFocusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSaveClick.invoke(categoryNameValueState.text) })
        )
        FilledTonalButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(6.dp),
            onClick = { onSaveClick.invoke(categoryNameValueState.text) }
        ) {
            Text(text = "Save")
        }
    }
}
