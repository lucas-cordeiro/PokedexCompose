package br.com.lucascordeiro.pokedex.data.network.httpclient.timeout

interface HttpTimeout {
    val connectTimeoutMillis: Long
    val socketTimeoutMillis: Long
    val requestTimeoutMillis: Long
}