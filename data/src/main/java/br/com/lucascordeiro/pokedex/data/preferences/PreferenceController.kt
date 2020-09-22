package br.com.lucascordeiro.pokedex.data.preferences

interface PreferenceController {
    var lastCacheTime: Long
    var needDownload: Boolean
}