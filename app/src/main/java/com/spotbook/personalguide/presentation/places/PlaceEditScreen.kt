package com.spotbook.personalguide.presentation.places

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.spotbook.personalguide.data.local.LocalPhotoStorage
import com.spotbook.personalguide.domain.model.PlaceStatus
import com.spotbook.personalguide.presentation.common.AdaptivePane
import com.spotbook.personalguide.presentation.common.photoModel

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
    val context = LocalContext.current
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val localPath = LocalPhotoStorage.savePhoto(context, uri)
            viewModel.onPhotoPathChange(localPath)
        }
    }

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
                OutlinedButton(
                    onClick = { photoPicker.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Выбрать фото")
                }
                photoModel(form.photoPath.ifBlank { form.serverPhotoPath })?.let { model ->
                    AsyncImage(
                        model = model,
                        contentDescription = "Фото места",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                }

                RatingDropdown(
                    rating = form.rating,
                    onRatingChange = viewModel::onRatingChange
                )

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
                    StatusButton(
                        text = group.name,
                        selected = form.groupId == group.localId,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.onGroupChange(group.localId)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RatingDropdown(
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = rating.toString(),
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            label = { Text("Оценка") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            (1..10).forEach { value ->
                DropdownMenuItem(
                    text = { Text(value.toString()) },
                    onClick = {
                        onRatingChange(value)
                        expanded = false
                    }
                )
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
