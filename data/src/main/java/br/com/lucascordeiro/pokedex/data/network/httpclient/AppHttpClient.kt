package br.com.lucascordeiro.pokedex.data.network.httpclient

import io.ktor.client.*

interface AppHttpClient {
    val httpClient: HttpClient
}