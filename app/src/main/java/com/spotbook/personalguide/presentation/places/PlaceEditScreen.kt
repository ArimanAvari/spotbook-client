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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(form.title, viewModel::onTitleChange, Modifier.fillMaxWidth(), label = { Text("Название") })
            OutlinedTextField(form.address, viewModel::onAddressChange, Modifier.fillMaxWidth(), label = { Text("Адрес") })
            OutlinedTextField(form.comment, viewModel::onCommentChange, Modifier.fillMaxWidth(), label = { Text("Комментарий") })
            OutlinedTextField(form.photoPath, viewModel::onPhotoPathChange, Modifier.fillMaxWidth(), label = { Text("Путь к фото") })

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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusButton("Хочу посетить", form.status == PlaceStatus.WANT_TO_VISIT) {
                    viewModel.onStatusChange(PlaceStatus.WANT_TO_VISIT)
                }
                StatusButton("Посещено", form.status == PlaceStatus.VISITED) {
                    viewModel.onStatusChange(PlaceStatus.VISITED)
                }
            }

            Text("Группа")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusButton("Без группы", form.groupId == null) { viewModel.onGroupChange(null) }
            }
            viewModel.state.groups.forEach { group ->
                OutlinedButton(onClick = { viewModel.onGroupChange(group.localId) }) {
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

@Composable
private fun StatusButton(text: String, selected: Boolean, onClick: () -> Unit) {
    if (selected) {
        Button(onClick = onClick) { Text(text) }
    } else {
        OutlinedButton(onClick = onClick) { Text(text) }
    }
}

