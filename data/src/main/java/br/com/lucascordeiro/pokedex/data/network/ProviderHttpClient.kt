package br.com.lucascordeiro.pokedex.data.network

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.url




fun provideHttpClient(baseUrl: String, serializer: KotlinxSerializer, logging: Logging, httpTimeout: HttpTimeout) =
    HttpClient(OkHttp){
        install(JsonFeature){
            this.serializer = serializer
        }
        install(Logging){
            logger = logging.logger
            level = logging.level
        }

        install(HttpTimeout){
            connectTimeoutMillis = httpTimeout.connectTimeoutMillis
            socketTimeoutMillis = httpTimeout.socketTimeoutMillis
            requestTimeoutMillis = httpTimeout.requestTimeoutMillis
        }

        defaultRequest {
            url(scheme = "https", host = baseUrl, path = this.url.encodedPath)
        }
    }