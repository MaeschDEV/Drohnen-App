package com.maeschdev.drohnenapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "ipAddress")

class UserPreferences(private val context: Context) {
    private val keyText1 = stringPreferencesKey("saved_text_1")
    private val keyText2 = stringPreferencesKey("saved_text_2")

    val savedText1: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[keyText1] ?: "" }

    val savedText2: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[keyText2] ?: "" }

    suspend fun saveText1(text: String) {
        context.dataStore.edit { preferences ->
            preferences[keyText1] = text
        }
    }

    suspend fun saveText2(text: String) {
        context.dataStore.edit { preferences ->
            preferences[keyText2] = text
        }
    }
}