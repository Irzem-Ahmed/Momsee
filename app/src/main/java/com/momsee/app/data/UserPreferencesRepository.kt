package com.momsee.app.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDate

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    val lmpDate: Flow<LocalDate?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferences.LMP_DATE_KEY]?.let { LocalDate.parse(it) }
        }

    val darkModeOverride: Flow<Boolean?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[UserPreferences.DARK_MODE_KEY]
        }

    suspend fun updateLmpDate(date: LocalDate) {
        dataStore.edit { preferences ->
            preferences[UserPreferences.LMP_DATE_KEY] = date.toString()
        }
    }

    suspend fun updateDarkMode(isDark: Boolean?) {
        dataStore.edit { preferences ->
            if (isDark == null) {
                preferences.remove(UserPreferences.DARK_MODE_KEY)
            } else {
                preferences[UserPreferences.DARK_MODE_KEY] = isDark
            }
        }
    }
}
