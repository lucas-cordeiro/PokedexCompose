package br.com.lucascordeiro.pokedex.domain.model

data class Pokemon(
    var id: Long,
    var name: String,
    var type: List<PokemonType>,
    var like: Boolean = false,
    var imageUrl: String
)