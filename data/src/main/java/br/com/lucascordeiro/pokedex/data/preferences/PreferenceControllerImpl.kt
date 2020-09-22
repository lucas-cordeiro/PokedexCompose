package br.com.lucascordeiro.pokedex.data.preferences

import android.content.Context
import br.com.lucascordeiro.pokedex.data.R

class PreferenceControllerImpl(private val context: Context) : PreferenceController {

    private val prefs = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    override var lastCacheTime: Long
        get() = prefs.getLong(PREF_KEY_LAST_CACHE_TIME, 0L)
        set(value) {
            prefs.edit().putLong(PREF_KEY_LAST_CACHE_TIME, value).apply()
        }

    override var needDownload: Boolean
        get() = prefs.getBoolean(PREF_KEY_LAST_NEED_DOWNLOAD, false)
        set(value) {
            prefs.edit().putBoolean(PREF_KEY_LAST_NEED_DOWNLOAD, value).apply()
        }

    companion object {
        const val PREF_KEY_LAST_CACHE_TIME = "PREF_KEY_LAST_CACHE_TIME"
        const val PREF_KEY_LAST_NEED_DOWNLOAD = "PREF_KEY_LAST_NEED_DOWNLOAD"
    }
}