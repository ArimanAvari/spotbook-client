package com.spotbook.personalguide.presentation.groups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import com.spotbook.personalguide.presentation.places.PlaceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupPlacesScreen(
    groupId: Long,
    groupViewModel: GroupViewModel,
    placeViewModel: PlaceViewModel,
    onBackClick: () -> Unit,
    onPlaceClick: (Long) -> Unit
) {
    val state = groupViewModel.state
    val group = state.groups.firstOrNull { it.localId == groupId }
    val places = state.places.filter { it.groupId == groupId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(group?.name ?: "Группа") },
                navigationIcon = { TextButton(onClick = onBackClick) { Text("Назад") } }
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
                if (places.isEmpty()) {
                    Text("В этой группе пока нет карточек")
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(places, key = { it.localId }) { place ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPlaceClick(place.localId) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(place.title, maxLines = 2)
                                    Text(place.address, maxLines = 2)
                                }
                                OutlinedButton(onClick = { placeViewModel.removeFromGroup(place.localId) }) {
                                    Text("Отвязать")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
