package com.zaragoza.contest.data.user.storage

import android.content.Context

class UserPreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString("USER_ID", userId).apply()
    }

    fun fetchUserId(): String? {
        return sharedPreferences.getString("USER_ID", null)
    }

}

