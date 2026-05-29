package com.spotbook.personalguide.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.spotbook.personalguide.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM local_groups WHERE sync_status != 'DELETED' ORDER BY updated_at DESC, local_id DESC")
    fun observeGroups(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM local_groups WHERE local_id = :localId LIMIT 1")
    suspend fun getGroupByLocalId(localId: Long): GroupEntity?

    @Query("SELECT * FROM local_groups WHERE server_id = :serverId LIMIT 1")
    suspend fun getGroupByServerId(serverId: Long): GroupEntity?

    @Query("SELECT * FROM local_groups WHERE sync_status IN (:statuses)")
    suspend fun getGroupsBySyncStatus(statuses: List<SyncStatus>): List<GroupEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<GroupEntity>)

    @Update
    suspend fun updateGroup(group: GroupEntity)

    @Delete
    suspend fun deleteGroup(group: GroupEntity)

    @Query("UPDATE local_groups SET sync_status = :syncStatus WHERE local_id = :localId")
    suspend fun updateSyncStatus(localId: Long, syncStatus: SyncStatus)

    @Query("DELETE FROM local_groups")
    suspend fun clearGroups()
}

