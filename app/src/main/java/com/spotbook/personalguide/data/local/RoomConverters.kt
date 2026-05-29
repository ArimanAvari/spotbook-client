package com.spotbook.personalguide.data.local

import androidx.room.TypeConverter
import com.spotbook.personalguide.domain.model.PlaceStatus
import com.spotbook.personalguide.domain.model.SyncStatus

class RoomConverters {
    @TypeConverter
    fun placeStatusToString(status: PlaceStatus): String = status.name

    @TypeConverter
    fun stringToPlaceStatus(value: String): PlaceStatus = PlaceStatus.valueOf(value)

    @TypeConverter
    fun syncStatusToString(status: SyncStatus): String = status.name

    @TypeConverter
    fun stringToSyncStatus(value: String): SyncStatus = SyncStatus.valueOf(value)
}

