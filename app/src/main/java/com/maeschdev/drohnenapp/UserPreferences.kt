package com.maeschdev.drohnenapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Erstellt eine einzige DataStorage-Instanz für die gesamte App
val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    // Schlüssel für gespeicherte Werte
    private val keyIpAddress = stringPreferencesKey("ip_address")
    private val keyPort = stringPreferencesKey("port")

    // Zugriff auf gespeicherte Daten als Flow (reagiert auf Änderungen)
    val savedIpAddress: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[keyIpAddress] ?: "0" }

    val savedPort: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[keyPort] ?: "0" }

    // Speichert Textwerte in DataStore
    suspend fun saveIpAddress(text: String) = context.dataStore.edit { it[keyIpAddress] = text }
    suspend fun savePort(text: String) = context.dataStore.edit { it[keyPort] = text }
}