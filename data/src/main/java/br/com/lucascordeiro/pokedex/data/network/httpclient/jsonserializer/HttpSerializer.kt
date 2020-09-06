package br.com.lucascordeiro.pokedex.data.network.httpclient.jsonserializer

import io.ktor.client.features.json.*

interface HttpSerializer {
    val serializer: JsonSerializer?
}