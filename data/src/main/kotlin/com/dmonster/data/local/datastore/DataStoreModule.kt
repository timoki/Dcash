package com.dmonster.data.local.datastore

import android.content.Context
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
import javax.inject.Inject


class DataStoreModule @Inject constructor(
    private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("dcash.db")

    private val mobileDataKey = booleanPreferencesKey("use_mobile_data")
    private val useLockScreenKey = booleanPreferencesKey("useLockScreen")
    private val loginIdKey = stringPreferencesKey("loginId")
    private val loginPwKey = stringPreferencesKey("loginPw")
    private val accessTokenKey = stringPreferencesKey("accessToken")
    private val refreshTokenKey = stringPreferencesKey("refreshToken")
    private val notShowingNewsViewTutorialKey = booleanPreferencesKey("notShowingNewsViewTutorial")

    val isMobileData: Flow<Boolean> = context.dataStore.data
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
        val getData = isMobileData.first()
        if (getData != use) {
            context.dataStore.edit { preferences ->
                preferences[mobileDataKey] = use
            }
        }
    }

    val isUseLockScreen: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[useLockScreenKey] ?: false
        }

    suspend fun putUseLockScreen(use: Boolean) {
        val getData = isUseLockScreen.first()
        if (getData != use) {
            context.dataStore.edit { preferences ->
                preferences[useLockScreenKey] = use
            }
        }
    }

    val getLoginId: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[loginIdKey] ?: ""
        }

    suspend fun putLoginId(value: String) {
        val getData = getLoginId.first()
        if (getData != value) {
            context.dataStore.edit { preferences ->
                preferences[loginIdKey] = value
            }
        }
    }

    val getLoginPw: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[loginPwKey] ?: ""
        }

    suspend fun putLoginPw(value: String) {
        val getData = getLoginPw.first()
        if (getData != value) {
            context.dataStore.edit { preferences ->
                preferences[loginPwKey] = value
            }
        }
    }

    val getAccessToken: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[accessTokenKey] ?: ""
        }

    suspend fun putAccessToken(value: String) {
        val getData = getAccessToken.first()
        if (getData != value) {
            context.dataStore.edit { preferences ->
                preferences[accessTokenKey] = value
            }
        }
    }

    val getRefreshToken: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[refreshTokenKey] ?: ""
        }

    suspend fun putRefreshToken(value: String) {
        val getData = getRefreshToken.first()
        if (getData != value) {
            context.dataStore.edit { preferences ->
                preferences[refreshTokenKey] = value
            }
        }
    }

    val isNotShowingNewsViewTutorial: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[notShowingNewsViewTutorialKey] ?: false
        }

    suspend fun putNotShowingNewsViewTutorial(use: Boolean) {
        val getData = isNotShowingNewsViewTutorial.first()
        if (getData != use) {
            context.dataStore.edit { preferences ->
                preferences[notShowingNewsViewTutorialKey] = use
            }
        }
    }
}