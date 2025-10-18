package com.example.testflow.ui.features.example

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.testflow.domain.models.ExampleItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamplePane(
    modifier: Modifier = Modifier,
    viewModel: ExampleViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    ExamplePane(
        state = state,
        onSendIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamplePane(
    state: ExampleState,
    modifier: Modifier = Modifier,
    onSendIntent: (ExampleIntent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Example List") })
        },
        modifier = modifier,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.items) { uiItem ->
                    ExampleItem(
                        item = uiItem.item,
                        isSelected = uiItem.isSelected,
                        onClick = { onSendIntent(ExampleIntent.ClickExample(uiItem.item.id)) }
                    )
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ExampleItem(item: ExampleItem, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.title, style = MaterialTheme.typography.bodyLarge)
        if (isSelected) {
            Icon(Icons.Default.Check, contentDescription = "Selected")
        }
    }
}

// ----- Preview -----
@Composable
@Preview(showBackground = true)
fun ExamplePanePreview() {
    ExamplePane(
        state = ExampleState(
            items = listOf(
                ExampleItemUiState(
                    item = ExampleItem(1, "First"),
                    isSelected = true,
                ),
                ExampleItemUiState(
                    item = ExampleItem(2, "Second"),
                    isSelected = true,
                ),
                ExampleItemUiState(
                    item = ExampleItem(3, "Third"),
                    isSelected = false,
                ),
            )
        )
    )
}