package com.spotbook.personalguide.presentation.places

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.spotbook.personalguide.domain.model.PlaceCard
import com.spotbook.personalguide.domain.model.PlaceStatus
import com.spotbook.personalguide.presentation.common.AdaptivePane
import com.spotbook.personalguide.presentation.common.photoModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListScreen(
    viewModel: PlaceViewModel,
    onAddClick: () -> Unit,
    onPlaceClick: (Long) -> Unit,
    onGroupsClick: () -> Unit,
    onSyncClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val state = viewModel.state
    val query = state.appliedSearchQuery
    val filteredPlaces = state.places.filter { place ->
        query.isBlank() ||
            place.title.contains(query, ignoreCase = true) ||
            place.address.contains(query, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои места") },
                actions = {
                    TextButton(onClick = onSyncClick) { Text("Синхр.") }
                    TextButton(onClick = onLogoutClick) { Text("Выйти") }
                }
            )
        },
        bottomBar = {
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                tonalElevation = 6.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onGroupsClick) {
                        Text("Группы")
                    }
                    FloatingActionButton(
                        onClick = onAddClick,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Text("+", fontSize = 28.sp)
                    }
                }
            }
        }
    ) { padding ->
        AdaptivePane(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Поиск по названию или адресу") },
                    singleLine = true
                )

                if (state.places.isEmpty()) {
                    Text("Карточек пока нет")
                } else if (filteredPlaces.isEmpty()) {
                    Text("Ничего не найдено")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredPlaces, key = { it.localId }) { place ->
                            PlaceListItem(place = place, onClick = { onPlaceClick(place.localId) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaceListItem(place: PlaceCard, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            photoModel(place.photoPath ?: place.serverPhotoPath)?.let { model ->
                AsyncImage(
                    model = model,
                    contentDescription = "Фото места",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )
            }
            Text(place.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(place.address, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = if (place.status == PlaceStatus.VISITED) "Посещено" else "Хочу посетить",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
