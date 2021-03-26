package dev.nelson.mot.main.presentations.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivityStatisticBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.util.extention.getDataBinding

class StatisticFragment : BaseFragment() {

    lateinit var binding: ActivityStatisticBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = getDataBinding(inflater, R.layout.activity_statistic, container)
//        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        binding.composeView.setContent { StatisticLayout() }
        return binding.root
    }

    @Composable
    fun PhotographerCard(modifier: Modifier = Modifier) {
        Row(
            modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.surface)
                .clickable(onClick = { /* Ignoring onClick */ })
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
            ) {
                // Image goes here
            }
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text("Alfred Sisley", fontWeight = FontWeight.Bold)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text("3 minutes ago", style = MaterialTheme.typography.body2)
                }
            }
        }
    }

    @Composable
    fun StatisticLayout() {
        MaterialTheme {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Statistic") },
                        navigationIcon = {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        })
                },
            ) {
                BodyContent()
            }
        }
    }

    @Composable
    fun BodyContent(modifier: Modifier = Modifier) {
        Column(modifier = modifier.padding(8.dp)) {
            Text(text = "Hi there!")
            Text(text = "Thanks for going through the Layouts codelab")
            LazyList()
        }
    }

    @Composable
    fun LazyList() {
        // We save the scrolling position with this state that can also
        // be used to programmatically scroll the list
        val scrollState = rememberLazyListState()

        LazyColumn(state = scrollState, modifier = Modifier.fillMaxWidth()) {
            items(100) {
                Row {
                    Text("Item #$it")
                }

            }
        }
    }

    @Preview
    @Composable
    fun PhotographerCardPreview() {
        StatisticLayout()
//        PhotographerCard()
    }

}

