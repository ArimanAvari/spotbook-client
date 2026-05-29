package com.spotbook.personalguide

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.spotbook.personalguide.data.remote.ApiService
import com.spotbook.personalguide.data.token.TokenStorage
import java.util.UUID
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BackendAuthInstrumentedTest {
    @Test
    fun registerLoginAndGetCurrentUser() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val tokenStorage = TokenStorage(context)
        val apiService = ApiService(BuildConfig.BACKEND_BASE_URL, tokenStorage)
        val email = "android_${UUID.randomUUID()}@example.com"
        val password = "password123"

        val registerResponse = apiService.register(email, password)
        tokenStorage.saveToken(registerResponse.token)

        assertEquals(email, registerResponse.user.email)
        assertTrue(registerResponse.token.isNotBlank())

        val currentUser = apiService.me()
        assertEquals(email, currentUser.email)

        tokenStorage.clearToken()
        assertTrue(tokenStorage.getToken().isNullOrBlank())

        val loginResponse = apiService.login(email, password)
        tokenStorage.saveToken(loginResponse.token)

        val loginUser = apiService.me()
        assertEquals(email, loginUser.email)
    }
}
