package br.com.lucascordeiro.pokedex.data.network.httpclient.logging

import io.ktor.client.features.logging.*

interface LoggingInterceptor{
    val log: (message: String) -> Unit
    val level: LogLevel
}