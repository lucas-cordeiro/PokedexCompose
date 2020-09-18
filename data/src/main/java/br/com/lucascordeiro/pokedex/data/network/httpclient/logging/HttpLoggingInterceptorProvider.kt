package br.com.lucascordeiro.pokedex.data.network.httpclient.logging

import android.util.Log
import io.ktor.client.features.logging.*

fun provideLoggingInterceptor(debug: Boolean, tag: String = "KTOR"): LoggingInterceptor {
    return object : LoggingInterceptor {
        override val log: (message: String) -> Unit
            get() = { Log.d(tag, it) }
        override val level = if (debug) LogLevel.ALL else LogLevel.NONE
    }
}