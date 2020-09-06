package br.com.lucascordeiro.pokedex.helper

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType

fun doGetFakeDataNetwork() = listOf(
    Pokemon(
        id = 1,
        name = "Bulbasaur",
        type = listOf(PokemonType.valueOf("GRASS")),
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
    ),
    Pokemon(
        id = 4,
        name = "Charmander",
        type = listOf(PokemonType.valueOf("FIRE")),
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png"
    ),
    Pokemon(
        id = 7,
        name = "Squirtle",
        type =listOf(PokemonType.valueOf("WATER")),
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png"
    )
)