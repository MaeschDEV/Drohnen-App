package com.maeschdev.drohnenapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "ipAddress")

class UserPreferences(private val context: Context) {
    private val KEY_TEXT_1 = stringPreferencesKey("saved_text_1")
    private val KEY_TEXT_2 = stringPreferencesKey("saved_text_2")

    val savedText1: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[KEY_TEXT_1] ?: "" }

    val savedText2: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[KEY_TEXT_2] ?: "0" }

    suspend fun saveText1(text: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TEXT_1] = text
        }
    }

    suspend fun saveText2(text: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TEXT_2] = text
        }
    }
}