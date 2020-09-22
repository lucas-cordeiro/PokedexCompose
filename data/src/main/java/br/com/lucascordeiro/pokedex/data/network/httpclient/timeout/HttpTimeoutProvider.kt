package br.com.lucascordeiro.pokedex.data.network.httpclient.timeout

fun provideHttpTimeout() = object : HttpTimeout {
    override val connectTimeoutMillis = 30 * 1000L
    override val requestTimeoutMillis = 30 * 1000L
    override val socketTimeoutMillis = 30 * 1000L
}