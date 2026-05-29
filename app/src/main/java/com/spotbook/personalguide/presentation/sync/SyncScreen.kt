package com.spotbook.personalguide.presentation.sync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spotbook.personalguide.presentation.common.AdaptivePane

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncScreen(
    viewModel: SyncViewModel,
    onBackClick: () -> Unit
) {
    val state = viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Синхронизация") },
                navigationIcon = { TextButton(onClick = onBackClick) { Text("Назад") } }
            )
        }
    ) { padding ->
        AdaptivePane(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            wideFraction = 0.62f
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = viewModel::exportData,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    Text("Экспорт на сервер")
                }
                OutlinedButton(
                    onClick = viewModel::importData,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    Text("Импорт с сервера")
                }
                Text(state.statusText)
                state.error?.let { Text(it) }
                if (state.isLoading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
