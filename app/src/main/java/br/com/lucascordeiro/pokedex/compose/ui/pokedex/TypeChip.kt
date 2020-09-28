package br.com.lucascordeiro.pokedex.compose.ui.pokedex

import br.com.lucascordeiro.pokedex.domain.model.PokemonType

data class TypeChip(
        var pokemonType: PokemonType,
        var checked: Boolean
)