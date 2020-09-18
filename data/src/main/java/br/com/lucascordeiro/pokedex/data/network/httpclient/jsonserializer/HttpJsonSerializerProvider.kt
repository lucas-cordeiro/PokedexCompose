package br.com.lucascordeiro.pokedex.data.network.httpclient.jsonserializer

import io.ktor.client.features.json.*

fun provideSerializer() = object : HttpSerializer {
    override val serializer: JsonSerializer?
        get() = GsonSerializer {
            serializeNulls()
        }
}