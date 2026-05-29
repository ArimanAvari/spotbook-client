package com.spotbook.personalguide.presentation.places

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spotbook.personalguide.domain.model.PlaceStatus
import com.spotbook.personalguide.presentation.common.AdaptivePane

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceEditScreen(
    title: String,
    viewModel: PlaceViewModel,
    placeId: Long?,
    onBackClick: () -> Unit,
    onSaved: () -> Unit
) {
    val form = viewModel.formState

    LaunchedEffect(placeId) {
        if (placeId != null) viewModel.startEdit(placeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
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
                OutlinedTextField(
                    value = form.title,
                    onValueChange = viewModel::onTitleChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Название") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = form.address,
                    onValueChange = viewModel::onAddressChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Адрес") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = form.comment,
                    onValueChange = viewModel::onCommentChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Комментарий") }
                )
                OutlinedTextField(
                    value = form.photoPath,
                    onValueChange = viewModel::onPhotoPathChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Путь к фото") },
                    singleLine = true
                )

                Text("Оценка")
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (1..5).forEach { rating ->
                        if (form.rating == rating) {
                            Button(onClick = { viewModel.onRatingChange(rating) }) { Text(rating.toString()) }
                        } else {
                            OutlinedButton(onClick = { viewModel.onRatingChange(rating) }) { Text(rating.toString()) }
                        }
                    }
                }

                Text("Статус")
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusButton(
                        text = "Хочу посетить",
                        selected = form.status == PlaceStatus.WANT_TO_VISIT,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.onStatusChange(PlaceStatus.WANT_TO_VISIT)
                    }
                    StatusButton(
                        text = "Посещено",
                        selected = form.status == PlaceStatus.VISITED,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.onStatusChange(PlaceStatus.VISITED)
                    }
                }

                Text("Группа")
                StatusButton(
                    text = "Без группы",
                    selected = form.groupId == null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    viewModel.onGroupChange(null)
                }
                viewModel.state.groups.forEach { group ->
                    OutlinedButton(
                        onClick = { viewModel.onGroupChange(group.localId) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (form.groupId == group.localId) "${group.name} *" else group.name)
                    }
                }

                viewModel.state.error?.let { Text(it) }
                Button(
                    onClick = { viewModel.savePlace(placeId, onSaved) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}

@Composable
private fun StatusButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (selected) {
        Button(onClick = onClick, modifier = modifier) { Text(text) }
    } else {
        OutlinedButton(onClick = onClick, modifier = modifier) { Text(text) }
    }
}
