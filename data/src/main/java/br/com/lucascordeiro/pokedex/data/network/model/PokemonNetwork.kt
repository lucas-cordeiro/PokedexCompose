package br.com.lucascordeiro.pokedex.data.network.model

data class PokemonNetwork(
    var id: Long? = null,
    var name: String? = null,
    var url: String? = null,
    var imageUrl: String? = null,
    var types: List<PokemonTypeNetwork>? = null
)