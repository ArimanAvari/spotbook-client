package com.spotbook.personalguide

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.spotbook.personalguide.data.local.AppDatabase
import com.spotbook.personalguide.data.local.GroupEntity
import com.spotbook.personalguide.data.local.PlaceEntity
import com.spotbook.personalguide.data.remote.ApiService
import com.spotbook.personalguide.data.repository.SyncRepositoryImpl
import com.spotbook.personalguide.data.token.TokenStorage
import com.spotbook.personalguide.domain.model.PlaceStatus
import com.spotbook.personalguide.domain.model.SyncStatus
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BackendSyncInstrumentedTest {
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun exportAndImportLocalChanges() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val tokenStorage = TokenStorage(context)
        val apiService = ApiService(BuildConfig.BACKEND_BASE_URL, tokenStorage)
        val email = "sync_${UUID.randomUUID()}@example.com"
        val password = "password123"
        val authResponse = apiService.register(email, password)
        tokenStorage.saveToken(authResponse.token)

        val now = Instant.now().toString()
        val groupLocalId = database.groupDao().insertGroup(
            GroupEntity(
                serverId = null,
                name = "Test group",
                syncStatus = SyncStatus.NOT_SYNCED,
                createdAt = now,
                updatedAt = now
            )
        )
        database.placeDao().insertPlace(
            PlaceEntity(
                serverId = null,
                title = "Test place",
                address = "Test address",
                photoPath = null,
                rating = 5,
                comment = "Created locally",
                status = PlaceStatus.WANT_TO_VISIT,
                groupId = groupLocalId,
                syncStatus = SyncStatus.NOT_SYNCED,
                createdAt = now,
                updatedAt = now
            )
        )

        val repository = SyncRepositoryImpl(
            apiService = apiService,
            placeDao = database.placeDao(),
            groupDao = database.groupDao()
        )

        repository.exportData()

        val syncedGroups = database.groupDao().getGroupsBySyncStatus(listOf(SyncStatus.SYNCED))
        val syncedPlaces = database.placeDao().getPlacesBySyncStatus(listOf(SyncStatus.SYNCED))
        assertEquals(1, syncedGroups.size)
        assertEquals(1, syncedPlaces.size)
        assertNotNull(syncedGroups.first().serverId)
        assertNotNull(syncedPlaces.first().serverId)
        assertEquals(syncedGroups.first().localId, syncedPlaces.first().groupId)

        repository.importData()

        val importedPlaces = database.placeDao().getPlacesBySyncStatus(listOf(SyncStatus.SYNCED))
        assertTrue(importedPlaces.any { it.title == "Test place" })

        val importedPlace = importedPlaces.first { it.title == "Test place" }
        database.placeDao().updatePlace(importedPlace.copy(syncStatus = SyncStatus.DELETED))
        repository.exportData()

        val afterDelete = database.placeDao().getPlacesBySyncStatus(listOf(SyncStatus.SYNCED))
        assertTrue(afterDelete.none { it.serverId == importedPlace.serverId })
    }
}

