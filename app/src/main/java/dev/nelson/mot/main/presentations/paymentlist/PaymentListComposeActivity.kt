package dev.nelson.mot.main.presentations.paymentlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.LiveData
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.presentations.base.EntryPointActivity

class PaymentListComposeActivity : EntryPointActivity() {

    companion object {
        fun getIntent(context: Context): Intent =
            Intent(context, PaymentListComposeActivity::class.java)
    }

    private val viewModel: PaymentListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PaymentListLayout() }
    }

    @Composable
    fun PaymentListLayout() {

        val fabShape = RoundedCornerShape(50)

        MaterialTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Payment List Compose") },
//                        navigationIcon = {
//                            IconButton(onClick = { finish() }) {
//                                Icon(
//                                    Icons.Filled.ArrowBack,
//                                    contentDescription = null
//                                )
//                            }
//                        },

                    )

                },
                bottomBar = {
                    BottomAppBar(cutoutShape = fabShape) {}
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {},
                        // We specify the same shape that we passed as the cutoutShape above.
                        shape = fabShape,
                        // We use the secondary color from the current theme. It uses the defaults when
                        // you don't specify a theme (this example doesn't specify a theme either hence
                        // it will just use defaults. Look at DarkModeActivity if you want to see an
                        // example of using themes.
                        backgroundColor = MaterialTheme.colors.secondary
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = null
                            )
                        }
                    }
                },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.Center,


                ) {
                BodyContent()
            }
        }
    }

    @Composable
    fun BodyContent() {
        PaymentList(viewModel.paymentListLiveData)
    }

    @Composable
    fun PaymentList(paymentListLiveData: LiveData<List<Payment>>) {
        // We save the scrolling position with this state that can also
        // be used to programmatically scroll the list
        val scrollState = rememberLazyListState()
        val paymentList by paymentListLiveData.observeAsState(initial = emptyList())
        if (paymentList.isEmpty()) {
            LiveDataLoadingComponent()
        } else {
            PaymentList(paymentList)
        }


    }

    @Composable
    fun PaymentList(paymentList: List<Payment>) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .background(Color.Magenta)
        ) {
            items(paymentList.size) {
                PaymentItem(payment = paymentList[it])
            }
        }
    }

    @Composable
    fun PaymentItem(payment: Payment) {
        Column(
            modifier = Modifier
                .background(Color.LightGray),
        ) {
            Row(
                modifier = Modifier
                    .background(Color.Magenta),
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.Cyan)
                        .weight(1.0f)
                        .fillMaxSize()
                ) {
                    Text(payment.name)
                    Text("asdl;kfhjlsdfjlkasdfjkl;sdfsdfasdfassadfasdfasfasdfdfasdfasdf")
                    payment.category?.name?.let { Text(it) }
                }
                Column(
                    modifier = Modifier
                        .background(Color.Green)
                        .padding(Dp(16f))
                        .align(alignment = Alignment.CenterVertically)
                ) {
                    Text(payment.cost.toString())
                }
            }
            RadioGroupHorizontal(radioOptions = listOf("on", "off", "none"))

        }
    }

    @Composable
    fun RadioGroupHorizontal(radioOptions: List<String>) {
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            radioOptions.forEach { text ->
                Column(
                    Modifier
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) }
                        ),

                    ) {
                    Row {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) }
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.body1.merge(),
                        )
                    }
                }
            }
        }
    }


    @Composable
    fun LiveDataLoadingComponent() {
        // Column is a composable that places its children in a vertical sequence. You
        // can think of it similar to a LinearLayout with the vertical orientation.
        // In addition we also pass a few modifiers to it.

        // You can think of Modifiers as implementations of the decorators pattern that are
        // used to modify the composable that its applied to. In this example, we configure the
        // Column composable to occupy the entire available width and height using
        // Modifier.fillMaxSize().
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // A pre-defined composable that's capable of rendering a circular progress indicator. It
            // honors the Material Design specification.
            CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally))
        }
    }

    @Preview
    @Composable
    fun LayoutPreview() {
        PaymentListLayout()
    }
}

