package br.com.lucascordeiro.pokedex.data.network

fun provideHttpTimeout() = object : HttpTimeout {
    override val connectTimeoutMillis = 15 * 1000L
    override val requestTimeoutMillis = 15 * 1000L
    override val socketTimeoutMillis = 15 * 1000L
}