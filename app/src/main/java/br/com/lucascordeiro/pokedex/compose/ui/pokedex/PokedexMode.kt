package br.com.lucascordeiro.pokedex.compose.ui.pokedex

sealed class PokedexMode {
    object PokemonList : PokedexMode()
    object Filter: PokedexMode()
}