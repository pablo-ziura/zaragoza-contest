package com.zaragoza.contest.data.user.storage

import android.content.Context
import com.zaragoza.contest.domain.UserPreferences

class UserPreferencesManager(private val context: Context) : UserPreferences {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    override fun saveUserId(userId: String) {
        sharedPreferences.edit().putString("USER_ID", userId).apply()
    }

    override fun fetchUserId(): String? {
        return sharedPreferences.getString("USER_ID", null)
    }
}

