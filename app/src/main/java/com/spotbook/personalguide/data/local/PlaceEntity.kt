package com.spotbook.personalguide.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spotbook.personalguide.domain.model.PlaceStatus
import com.spotbook.personalguide.domain.model.SyncStatus

@Entity(tableName = "local_place_cards")
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val localId: Long = 0,

    @ColumnInfo(name = "server_id")
    val serverId: Long?,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "photo_path")
    val photoPath: String?,

    @ColumnInfo(name = "rating")
    val rating: Int,

    @ColumnInfo(name = "comment")
    val comment: String,

    @ColumnInfo(name = "status")
    val status: PlaceStatus,

    @ColumnInfo(name = "group_id")
    val groupId: Long?,

    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "updated_at")
    val updatedAt: String
)

