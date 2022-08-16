package dev.nelson.mot.main.presentations.payment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.presentations.ui.theme.MotTheme
import kotlinx.coroutines.delay
import java.util.Calendar

@AndroidEntryPoint
class PaymentDetailsComposeActivity : ComponentActivity() {

    private val viewModel: PaymentDetailsComposeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            MotTheme {
                val date by viewModel.date.observeAsState("")
                PaymentDetailsLayout(
                    nameLiveData = viewModel.paymentName,
                    costLiveData = viewModel.cost,
                    date = date,
                    messageLiveData = viewModel.message,
                    onNameChange = { viewModel.paymentName.value = it },
                    onCostChange = { viewModel.cost.value = it },
                    onMessageChange = { viewModel.message.value = it },
                    onSaveClick = { viewModel.onSaveClick() },
                    onDateClick = { viewModel.onDateClick() }
                )
            }
        }
        initListeners()
    }

    private fun initListeners() {
        viewModel.finishAction.observe(this) { finish() }
        viewModel.onDateClickAction.observe(this) { openDatePickerDialog() }
    }

    private fun openDatePickerDialog() {
        val cldr: Calendar = Calendar.getInstance()
        val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
        val month: Int = cldr.get(Calendar.MONTH)
        val year: Int = cldr.get(Calendar.YEAR)
        val picker = DatePickerDialog(
            this,
            { _, selectedYear, monthOfYear, dayOfMonth -> run { viewModel.onDateSet(selectedYear, monthOfYear, dayOfMonth) } },
            year,
            month,
            day
        )
        picker.show()
    }

}

@Preview(showBackground = true)
@Composable
fun PaymentDetailsLayoutPreview() {
    PaymentDetailsLayout(
        nameLiveData = MutableLiveData(""),
        costLiveData = MutableLiveData(""),
        date = "1/1/2022",
        messageLiveData = MutableLiveData(""),
        onNameChange = {},
        onCostChange = {},
        onMessageChange = {},
        onSaveClick = {},
        onDateClick = {}
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PaymentDetailsLayout(
    nameLiveData: MutableLiveData<String>,
    costLiveData: MutableLiveData<String>,
    date: String,
    messageLiveData: MutableLiveData<String>,
    onNameChange: (String) -> Unit,
    onCostChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val name = nameLiveData.value.orEmpty()
    val cost = costLiveData.value.orEmpty()
    val message = messageLiveData.value.orEmpty()
    val nameFocusRequester = remember { FocusRequester() }
    var nameFieldValueState by remember { mutableStateOf(TextFieldValue(text = name, selection = TextRange(name.length))) }
    var costFieldValueState by remember { mutableStateOf(TextFieldValue(text = cost, selection = TextRange(cost.length))) }
    var messageFieldValueState by remember { mutableStateOf(TextFieldValue(text = message, selection = TextRange(message.length))) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit, block = {
        delay(200) // <-- This is crucial.
        nameFocusRequester.requestFocus()
    })
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.verticalScroll(
                state = rememberScrollState(),
                enabled = true
            )
        ) {
            Row {
                TextField(
                    modifier = Modifier
                        .focusRequester(nameFocusRequester)
                        .weight(2f),
//                    value = if (LocalInspectionMode.current) "preview new payment" else name,
                    value = nameFieldValueState,
                    singleLine = true,
                    maxLines = 1,
                    onValueChange = {
                        nameFieldValueState = it
                        onNameChange.invoke(nameFieldValueState.text)
                    },
                    placeholder = { Text(text = "new payment") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
                TextField(
                    modifier = Modifier.weight(1f),
                    value = costFieldValueState,
                    singleLine = true,
                    maxLines = 1,
                    onValueChange = {
                        costFieldValueState = it
                        onCostChange.invoke(costFieldValueState.text)
                    },
                    placeholder = { Text(text = "0.0") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                )
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onDateClick.invoke()
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.EditCalendar,
                    contentDescription = "date icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = date,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = messageFieldValueState,
                onValueChange = {
                    messageFieldValueState = it
                    onMessageChange.invoke(messageFieldValueState.text)
                },
                placeholder = { Text(text = "message") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSaveClick.invoke() })
            )
            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp),
                onClick = onSaveClick
            ) {
                Text(text = "Save")
            }
        }
    }
}



