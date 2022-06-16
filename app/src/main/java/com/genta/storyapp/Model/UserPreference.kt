package com.genta.storyapp.Model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[IDKEY] ?:"",
                preferences[NAMEKEY] ?:"",
                preferences[STATE_KEY] ?:false,
                preferences[TOKENKEY] ?: ""
            )
        }
    }



    suspend fun login(userModel: UserModel) {
        dataStore.edit { preferences ->
            preferences[IDKEY] =  userModel.userId
            preferences[NAMEKEY] = userModel.name
            preferences[STATE_KEY] = userModel.isLogin
            preferences[TOKENKEY] = userModel.token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false

        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val IDKEY= stringPreferencesKey("id")
        private val NAMEKEY= stringPreferencesKey("name")
        private val TOKENKEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}