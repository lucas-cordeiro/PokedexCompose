package br.com.lucascordeiro.pokedex.data.network.model

data class ResponsePokemonNetwork<T>(
    var count: Long? = null,
    var next: String? = null,
    var previous: String? = null,
    var results: List<T>? = null
)