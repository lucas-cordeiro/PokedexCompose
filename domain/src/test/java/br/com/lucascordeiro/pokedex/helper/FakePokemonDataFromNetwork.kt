package br.com.lucascordeiro.pokedex.helper

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType

fun doGetFakeDataNetwork() = listOf(
    Pokemon(
        id = 1,
        name = "Bulbasaur",
        imageUrl = "https://raw.githubusercontent.com/" +
                "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                "1.png",
        type = listOf(PokemonType.GRASS)
    ),
    Pokemon(
        id = 4,
        name = "Charmander",
        imageUrl = "https://raw.githubusercontent.com/" +
                "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                "4.png",
        type = listOf(PokemonType.FIRE)
    ),
    Pokemon(
        id = 7,
        name = "Squirtle",
        imageUrl = "https://raw.githubusercontent.com/" +
                "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                "7.png",
        type = listOf(PokemonType.WATER)
    ),
    Pokemon(
        id = 10,
        name = "Caterpie",
        imageUrl = "https://raw.githubusercontent.com/" +
                "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                "10.png",
        type = listOf(PokemonType.BUG)
    ),
    Pokemon(
        id = 13,
        name = "Weedle",
        imageUrl = "https://raw.githubusercontent.com/" +
                "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                "13.png",
        type = listOf(PokemonType.POISON)
    ),
    Pokemon(
        id = 16,
        name = "Pidgey",
        imageUrl = "https://raw.githubusercontent.com/" +
                "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                "16.png",
        type = listOf(PokemonType.FIGHTING)
    )
)