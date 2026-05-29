package com.spotbook.personalguide.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spotbook.personalguide.domain.model.SyncStatus

@Entity(tableName = "local_groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val localId: Long = 0,

    @ColumnInfo(name = "server_id")
    val serverId: Long?,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "updated_at")
    val updatedAt: String
)

