package br.com.lucascordeiro.pokedex.data.network

interface HttpTimeout {
    val connectTimeoutMillis: Long
    val socketTimeoutMillis: Long
    val requestTimeoutMillis: Long
}