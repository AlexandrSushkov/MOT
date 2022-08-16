package dev.nelson.mot.main.presentations.category_details.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.presentations.category_details.CategoryDetailsViewModel

@AndroidEntryPoint
class CategoryDetailsComposeFragment : BottomSheetDialogFragment() {

    val viewModel: CategoryDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                CategoryDetailsComposeLayout(
                    categoryNameLiveData = viewModel.categoryName,
                    onCategoryNameChanged = { viewModel.categoryName.value = it },
                    onSaveClick = { viewModel.onSaveClick() }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        viewModel.closeScreenAction.observe(viewLifecycleOwner) { dismiss() }
    }

    companion object {
        fun getInstance(category: Category? = null): CategoryDetailsComposeFragment {
            val bundle = Bundle().apply {
                putParcelable("category", category)
            }

            return CategoryDetailsComposeFragment().apply { arguments = bundle }
        }
    }
}

@Composable
fun CategoryDetailsComposeLayout(
    categoryNameLiveData: MutableLiveData<String>,
    onCategoryNameChanged: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    val categoryName = categoryNameLiveData.observeAsState().value.orEmpty()
    val categoryNameFocusRequester = remember { FocusRequester.Default }
    var categoryNameValueState by remember { mutableStateOf(TextFieldValue(text = categoryName, selection = TextRange(categoryName.length))) }

    LaunchedEffect(key1 = Unit, block = { categoryNameFocusRequester.requestFocus() })
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = categoryNameValueState,
            onValueChange = { categoryNameValueState = it },
            placeholder = { Text(text = "new payment") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(categoryNameFocusRequester),
        )
        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(6.dp),
            onClick = {
                onCategoryNameChanged.invoke(categoryNameValueState.text)
                onSaveClick.invoke()
            }
        ) {
            Text(text = "Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryDetailsComposeLayoutPreview() {
    CategoryDetailsComposeLayout(
        categoryNameLiveData = MutableLiveData("category"),
        onCategoryNameChanged = {}
    ) {}
}
