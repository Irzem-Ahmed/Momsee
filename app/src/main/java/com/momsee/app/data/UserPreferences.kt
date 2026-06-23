package com.momsee.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "momsee_prefs")

object UserPreferences {
    val LMP_DATE_KEY = stringPreferencesKey("lmp_date")
    val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
}
