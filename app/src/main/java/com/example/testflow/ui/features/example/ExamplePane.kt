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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.testflow.domain.models.Device
import com.example.testflow.domain.models.User

private const val USER_CONTENT_TYPE = "USER"
private const val DEVICE_CONTENT_TYPE = "DEVICE"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamplePane(
    modifier: Modifier = Modifier,
    viewModel: ExampleViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    ExamplePane(
        state = state,
        onSendIntent = viewModel::receiveIntent,
        modifier = modifier,
    )
    LaunchedEffect(Unit) {
        viewModel.receiveIntent(ExampleIntent.GetRandomNumber)
    }
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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onSendIntent(ExampleIntent.GetRandomNumber) },
                text = {
                    Text("${state.randomNumber}")
                },
                icon = {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ItemsList(
                items = state.items,
                onSendIntent = onSendIntent,
                modifier = Modifier.fillMaxSize(),
            )
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun ItemsList(
    items: List<ExampleItemUiState>,
    modifier: Modifier = Modifier,
    onSendIntent: (ExampleIntent) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items.forEach { uiItem ->
            item("${uiItem.user.id}", USER_CONTENT_TYPE) {
                UserItem(
                    item = uiItem.user,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )
            }
            items(
                uiItem.devices,
                { "${uiItem.user.id}-${it.device.id}" },
                { DEVICE_CONTENT_TYPE }) { deviceItem ->
                DeviceItem(
                    item = deviceItem.device,
                    isSelected = deviceItem.isSelected,
                    onClick = { onSendIntent(ExampleIntent.ClickExample(deviceItem.device.id)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun UserItem(
    item: User,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun DeviceItem(
    item: Device,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
        if (isSelected) {
            Icon(Icons.Default.Check, contentDescription = null)
        }
    }
}

// ----- Preview -----
@Composable
@Preview(showBackground = true)
fun ExamplePanePreview() {
    ExamplePane(
        state = ExampleState(
            isLoading = false,
            items = listOf(
                ExampleItemUiState(
                    user = User(1, "User #1"),
                    devices = listOf(
                        ExampleDeviceItemUiState(
                            device = Device(1, "First", userId = 1),
                            isSelected = true,
                        ),
                        ExampleDeviceItemUiState(
                            device = Device(2, "Second", userId = 1),
                            isSelected = true,
                        ),
                    )
                ),
                ExampleItemUiState(
                    user = User(2, "User #2"),
                    devices = listOf(
                        ExampleDeviceItemUiState(
                            device = Device(3, "Third", userId = 2),
                            isSelected = true,
                        ),
                    )
                ),
            )
        )
    )
}