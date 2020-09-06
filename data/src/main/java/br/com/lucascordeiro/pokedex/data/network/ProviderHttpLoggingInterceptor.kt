package br.com.lucascordeiro.pokedex.data.network

import android.util.Log
import io.ktor.client.features.logging.*

fun provideLoggingInterceptor(debug: Boolean) : Logging{
    val  logger = object : Logger {
        override fun log(message: String) {
            Log.d("KTOR", message)
        }
    }
    val level = if(debug) LogLevel.ALL else LogLevel.NONE
    return Logging(logger, level)
}