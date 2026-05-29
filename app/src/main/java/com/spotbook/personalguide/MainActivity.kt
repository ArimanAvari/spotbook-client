package com.spotbook.personalguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.spotbook.personalguide.data.local.AppDatabase
import com.spotbook.personalguide.data.remote.ApiService
import com.spotbook.personalguide.data.repository.AuthRepositoryImpl
import com.spotbook.personalguide.data.repository.SyncRepositoryImpl
import com.spotbook.personalguide.data.token.TokenStorage
import com.spotbook.personalguide.presentation.navigation.AppNavGraph
import com.spotbook.personalguide.presentation.theme.SpotBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.create(this)
        val tokenStorage = TokenStorage(this)
        val apiService = ApiService(BuildConfig.BACKEND_BASE_URL, tokenStorage)
        val authRepository = AuthRepositoryImpl(apiService, tokenStorage)
        val syncRepository = SyncRepositoryImpl(
            context = applicationContext,
            apiService = apiService,
            placeDao = database.placeDao(),
            groupDao = database.groupDao()
        )

        setContent {
            SpotBookTheme {
                AppNavGraph(
                    database = database,
                    authRepository = authRepository,
                    syncRepository = syncRepository
                )
            }
        }
    }
}
