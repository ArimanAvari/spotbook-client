package com.spotbook.personalguide.presentation.sync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Обмен данными",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = { TextButton(onClick = onBackClick) { Text("Назад") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Выберите направление синхронизации",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                SyncActionCard(
                    title = "Отправить данные на сервер",
                    description = "Загрузить новые и изменённые карточки, группы и фотографии.",
                    buttonText = "Экспортировать",
                    enabled = !state.isLoading,
                    onClick = viewModel::exportData
                )

                SyncActionCard(
                    title = "Загрузить данные с сервера",
                    description = "Получить актуальные карточки и группы и сохранить их на устройстве.",
                    buttonText = "Импортировать",
                    enabled = !state.isLoading,
                    primaryAction = false,
                    onClick = viewModel::importData
                )

                SyncStatusCard(state = state)

                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun SyncActionCard(
    title: String,
    description: String,
    buttonText: String,
    enabled: Boolean,
    primaryAction: Boolean = true,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (primaryAction) {
                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled
                ) {
                    Text(buttonText)
                }
            } else {
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled
                ) {
                    Text(buttonText)
                }
            }
        }
    }
}

@Composable
private fun SyncStatusCard(state: SyncState) {
    val containerColor = when {
        state.error != null -> MaterialTheme.colorScheme.errorContainer
        state.isSuccess -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = when {
        state.error != null -> MaterialTheme.colorScheme.onErrorContainer
        state.isSuccess -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Состояние",
                style = MaterialTheme.typography.titleMedium,
                color = contentColor
            )
            Text(
                text = state.statusText,
                color = contentColor
            )
            state.error?.let {
                Text(
                    text = it,
                    color = contentColor
                )
            }
        }
    }
}
