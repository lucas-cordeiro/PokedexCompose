package br.com.lucascordeiro.pokedex.data.network.httpclient

import br.com.lucascordeiro.pokedex.data.network.httpclient.jsonserializer.HttpSerializer
import br.com.lucascordeiro.pokedex.data.network.httpclient.logging.LoggingInterceptor
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

fun provideHttpClient(
    baseUrl: String,
    httpSerializer: HttpSerializer,
    logging: LoggingInterceptor,
    httpTimeout: br.com.lucascordeiro.pokedex.data.network.httpclient.timeout.HttpTimeout
) = object : AppHttpClient {
    override val httpClient: HttpClient
        get() = HttpClient(OkHttp) {
            install(JsonFeature) {
                this.serializer = httpSerializer.serializer
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        logging.log(message)
                    }
                }
                level = logging.level
            }

            install(HttpTimeout) {
                connectTimeoutMillis = httpTimeout.connectTimeoutMillis
                socketTimeoutMillis = httpTimeout.socketTimeoutMillis
                requestTimeoutMillis = httpTimeout.requestTimeoutMillis
            }

            defaultRequest {
                if (method != HttpMethod.Get) contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)

                url(scheme = "https", host = baseUrl, path = this.url.encodedPath)
            }
        }
}