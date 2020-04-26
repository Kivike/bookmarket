package fi.oulu.bookmarket2020

import android.content.Context
import android.content.SharedPreferences
import fi.oulu.bookmarket2020.model.AppDatabase
import fi.oulu.bookmarket2020.model.User
import org.jetbrains.anko.defaultSharedPreferences

class Session constructor(private val context: Context) {
    private val preferences: SharedPreferences = context.defaultSharedPreferences

    companion object {
        const val KEY_LOGGED_USER_EMAIL = "loggedUserEmail"
    }

    /**
     * Set session user ID
     */
    fun setLoggedInUser(user: User?): Session {
        if (user != null) {
            preferences.edit().putString(KEY_LOGGED_USER_EMAIL, user.email).apply()
        } else {
            preferences.edit().putString(KEY_LOGGED_USER_EMAIL, null).apply()
        }
        return this
    }

    /**
     * Get session user ID
     */
    fun getLoggedInUser(): User? {
        val email = preferences.getString(KEY_LOGGED_USER_EMAIL, "")

        if (email == null || email.isEmpty()) {
            return null
        }
        return AppDatabase.get(context).userDao().getUser(email)
    }
}