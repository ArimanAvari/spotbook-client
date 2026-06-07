package com.spotbook.personalguide.presentation.places

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = { TextButton(onClick = onBackClick) { Text("Назад") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 4.dp,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = { viewModel.savePlace(placeId, onSaved) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text("Сохранить")
                }
            }
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
                FormSection(title = "Основная информация") {
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
                }

                FormSection(title = "Фотография") {
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
                }

                FormSection(title = "Оценка и статус") {
                    RatingDropdown(
                        rating = form.rating,
                        onRatingChange = viewModel::onRatingChange
                    )
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

                FormSection(title = "Комментарий") {
                    OutlinedTextField(
                        value = form.comment,
                        onValueChange = viewModel::onCommentChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Комментарий") },
                        minLines = 3
                    )
                }

                FormSection(title = "Группа") {
                    StatusButton(
                        text = "Без группы",
                        selected = form.groupId == null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.onGroupChange(null)
                    }
                    if (viewModel.state.groups.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(208.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(viewModel.state.groups, key = { it.localId }) { group ->
                                StatusButton(
                                    text = group.name,
                                    selected = form.groupId == group.localId,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    viewModel.onGroupChange(group.localId)
                                }
                            }
                        }
                    }
                }

                viewModel.state.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun FormSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            content()
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

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Оценка")
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .menuAnchor()
                    .width(100.dp)
            ) {
                Text("$rating ▼")
            }
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(100.dp)
                    .height(144.dp)
            ) {
                (1..10).forEach { value ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = value.toString(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        },
                        onClick = {
                            onRatingChange(value)
                            expanded = false
                        }
                    )
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
