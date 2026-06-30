package com.momsee.app.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.io.IOException
import java.time.LocalDate

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    val doctorVisits: Flow<List<DoctorVisit>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val json = preferences[UserPreferences.DOCTOR_VISITS_KEY] ?: "[]"
            try {
                Json.decodeFromString<List<DoctorVisit>>(json)
            } catch (_: Exception) {
                emptyList()
            }
        }

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

    suspend fun addDoctorVisit(visit: DoctorVisit) {
        dataStore.edit { preferences ->
            val currentJson = preferences[UserPreferences.DOCTOR_VISITS_KEY] ?: "[]"
            val currentList = try {
                Json.decodeFromString<List<DoctorVisit>>(currentJson).toMutableList()
            } catch (_: Exception) {
                mutableListOf()
            }
            currentList.add(visit)
            preferences[UserPreferences.DOCTOR_VISITS_KEY] = Json.encodeToString(currentList)
        }
    }

    suspend fun deleteDoctorVisit(visitId: String) {
        dataStore.edit { preferences ->
            val currentJson = preferences[UserPreferences.DOCTOR_VISITS_KEY] ?: "[]"
            val currentList = try {
                Json.decodeFromString<List<DoctorVisit>>(currentJson).toMutableList()
            } catch (_: Exception) {
                mutableListOf()
            }
            currentList.removeAll { it.id == visitId }
            preferences[UserPreferences.DOCTOR_VISITS_KEY] = Json.encodeToString(currentList)
        }
    }
}
