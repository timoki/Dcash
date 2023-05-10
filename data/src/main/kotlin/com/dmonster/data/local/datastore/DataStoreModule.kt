package com.dmonster.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreModule(
    private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("dcash.db")

    fun<T> getPreferencesData(
        dataType: Preferences.Key<T>
    ): Flow<T> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[dataType] ?: throw Exception()
            }
    }

    suspend fun<T> putPreferencesData(
        dataType: Preferences.Key<T>,
        value: T
    ) {
        val getData = getPreferencesData(dataType).first()
        if (getData != value) {
            context.dataStore.edit { preferences ->
                preferences[dataType] = value
            }
        }
    }

    companion object {
        val MOBILE_DATA = booleanPreferencesKey("use_mobile_data")
        val USE_LOCK_SCREEN = booleanPreferencesKey("useLockScreen")
        val LOGIN_ID = stringPreferencesKey("loginId")
        val LOGIN_PW = stringPreferencesKey("loginPw")
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
    }
}