package br.com.lucascordeiro.pokedex.data.network

import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

private val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true, encodeDefaults = false, isLenient = true))

fun provideSerializer() : KotlinxSerializer{
    return KotlinxSerializer(json)
}