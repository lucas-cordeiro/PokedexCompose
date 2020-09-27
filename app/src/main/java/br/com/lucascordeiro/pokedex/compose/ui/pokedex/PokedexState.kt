package br.com.lucascordeiro.pokedex.compose.ui.pokedex

sealed class PokedexState {
    object PokemonList : PokedexState()
    object Filter: PokedexState()
}