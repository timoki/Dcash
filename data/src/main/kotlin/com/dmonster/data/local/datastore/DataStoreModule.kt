package com.dmonster.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreModule(
    private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("rewordApp.db")

    private val mobileDataKey = booleanPreferencesKey("use_mobile_data")
    private val useLockScreenKey = booleanPreferencesKey("useLockScreen")

    val getUserMobileData: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[mobileDataKey] ?: false
        }

    suspend fun putUseMobileData(use: Boolean) {
        val getData = getUserMobileData.first()
        if (getData != use) {
            context.dataStore.edit { preferences ->
                preferences[mobileDataKey] = use
            }
        }
    }

    val isUseLockScreen: Flow<Boolean> = context.dataStore.data
        .catch { e ->
            if (e is IOException) {
                emit(emptyPreferences())
            } else {
                throw e
            }
        }.map { preferences ->
            preferences[useLockScreenKey] ?: false
        }

    suspend fun setUseLockScreen(use: Boolean) {
        val getData = isUseLockScreen.first()
        if (getData != use) {
            context.dataStore.edit { preferences ->
                preferences[useLockScreenKey] = use
            }
        }
    }
}