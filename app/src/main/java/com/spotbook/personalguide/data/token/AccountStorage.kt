package com.spotbook.personalguide.data.token

import android.content.Context
import com.spotbook.personalguide.domain.model.User

class AccountStorage(context: Context) {
    private val preferences = context.getSharedPreferences("account", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        preferences.edit()
            .putLong(KEY_USER_ID, user.id)
            .putString(KEY_EMAIL, user.email)
            .apply()
    }

    fun getUserId(): Long? {
        val id = preferences.getLong(KEY_USER_ID, NO_USER)
        return id.takeIf { it != NO_USER }
    }

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_EMAIL = "email"
        private const val NO_USER = -1L
    }
}
