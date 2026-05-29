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
interface PlaceDao {
    @Query("SELECT * FROM local_place_cards WHERE sync_status != 'DELETED' ORDER BY updated_at DESC, local_id DESC")
    fun observePlaces(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM local_place_cards WHERE group_id = :groupId AND sync_status != 'DELETED' ORDER BY updated_at DESC, local_id DESC")
    fun observePlacesByGroup(groupId: Long): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM local_place_cards WHERE local_id = :localId LIMIT 1")
    suspend fun getPlaceByLocalId(localId: Long): PlaceEntity?

    @Query("SELECT * FROM local_place_cards WHERE server_id = :serverId LIMIT 1")
    suspend fun getPlaceByServerId(serverId: Long): PlaceEntity?

    @Query("SELECT * FROM local_place_cards WHERE sync_status IN (:statuses)")
    suspend fun getPlacesBySyncStatus(statuses: List<SyncStatus>): List<PlaceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: PlaceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceEntity>)

    @Update
    suspend fun updatePlace(place: PlaceEntity)

    @Delete
    suspend fun deletePlace(place: PlaceEntity)

    @Query("UPDATE local_place_cards SET group_id = NULL, sync_status = :syncStatus WHERE local_id = :localId")
    suspend fun removePlaceFromGroup(localId: Long, syncStatus: SyncStatus = SyncStatus.NOT_SYNCED)

    @Query("UPDATE local_place_cards SET sync_status = :syncStatus WHERE local_id = :localId")
    suspend fun updateSyncStatus(localId: Long, syncStatus: SyncStatus)

    @Query("DELETE FROM local_place_cards")
    suspend fun clearPlaces()
}

