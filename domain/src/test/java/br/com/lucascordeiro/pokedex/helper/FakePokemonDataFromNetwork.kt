package br.com.lucascordeiro.pokedex.helper

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType

fun doGetFakeDataNetwork() = listOf(
    Pokemon(
        id = 1,
        name = "Bulbasaur",
        type = PokemonType("Grass"),
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
    ),
    Pokemon(
        id = 4,
        name = "Charmander",
        type = PokemonType("Fire"),
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png"
    ),
    Pokemon(
        id = 7,
        name = "Squirtle",
        type = PokemonType("Water"),
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png"
    )
)