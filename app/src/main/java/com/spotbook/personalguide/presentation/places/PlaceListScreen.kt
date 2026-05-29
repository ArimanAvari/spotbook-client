package com.spotbook.personalguide.presentation.places

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.spotbook.personalguide.domain.model.PlaceCard
import com.spotbook.personalguide.domain.model.PlaceStatus
import com.spotbook.personalguide.presentation.common.AdaptivePane

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои места") },
                actions = {
                    TextButton(onClick = onSyncClick) { Text("Синхр.") }
                    TextButton(onClick = onLogoutClick) { Text("Выйти") }
                }
            )
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
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onAddClick) { Text("Добавить") }
                    OutlinedButton(onClick = onGroupsClick) { Text("Группы") }
                }

                if (state.places.isEmpty()) {
                    Text("Карточек пока нет")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.places, key = { it.localId }) { place ->
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
            Text(place.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(place.address, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = if (place.status == PlaceStatus.VISITED) "Посещено" else "Хочу посетить",
                color = MaterialTheme.colorScheme.primary
            )
            place.photoPath?.let {
                Text("Фото: $it", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
