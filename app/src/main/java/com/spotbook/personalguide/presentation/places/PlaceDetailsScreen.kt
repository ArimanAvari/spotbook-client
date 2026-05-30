package com.spotbook.personalguide.presentation.places

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.spotbook.personalguide.domain.model.PlaceStatus
import com.spotbook.personalguide.presentation.common.AdaptivePane
import com.spotbook.personalguide.presentation.common.photoModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsScreen(
    viewModel: PlaceViewModel,
    placeId: Long,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleted: () -> Unit
) {
    val state = viewModel.state
    val place = state.places.firstOrNull { it.localId == placeId }
    val groupName = state.groups.firstOrNull { it.localId == place?.groupId }?.name

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Карточка") },
                navigationIcon = { TextButton(onClick = onBackClick) { Text("Назад") } }
            )
        }
    ) { padding ->
        if (place == null) {
            Text("Карточка не найдена", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }

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
                Text(place.title, style = MaterialTheme.typography.headlineSmall)
                val visiblePhotoPath = place.photoPath ?: place.serverPhotoPath
                photoModel(visiblePhotoPath)?.let { model ->
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
                Text("Адрес: ${place.address}")
                Text("Оценка: ${place.rating}")
                Text("Статус: ${if (place.status == PlaceStatus.VISITED) "Посещено" else "Хочу посетить"}")
                Text("Комментарий: ${place.comment.ifBlank { "-" }}")
                Text("Группа: ${groupName ?: "-"}")
                if (visiblePhotoPath.isNullOrBlank()) {
                    Text("Фото: -")
                }
                Button(onClick = onEditClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Редактировать")
                }
                OutlinedButton(
                    onClick = { viewModel.deletePlace(placeId, onDeleted) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Удалить")
                }
            }
        }
    }
}
