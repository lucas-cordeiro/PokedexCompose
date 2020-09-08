package br.com.lucascordeiro.pokedex.data.preferences

import android.content.Context
import br.com.lucascordeiro.pokedex.data.R


class PreferenceControllerImpl(private val context: Context) : PreferenceController {

    private val prefs = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    override var lastCacheTime: Long
        get() = prefs.getLong(PREF_KEY_LAST_CAHCE_TIME, 0L)
        set(value) {
            prefs.edit().putLong(PREF_KEY_LAST_CAHCE_TIME, value).apply()
        }

    companion object{
        const val PREF_KEY_LAST_CAHCE_TIME = "PREF_KEY_LAST_CAHCE_TIME"
    }
}