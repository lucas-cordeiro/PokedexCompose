package br.com.lucascordeiro.pokedex.compose.di.component

import br.com.lucascordeiro.pokedex.compose.ui.home.HomeViewModel
import br.com.lucascordeiro.pokedex.compose.ui.pokedex.PokedexViewModel
import br.com.lucascordeiro.pokedex.domain.usecase.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class PokedexComponent : KoinComponent {
    val pokemonListUseCase: PokemonsListUseCase by inject()
    val pokemonDetailUseCase: PokemonDetailUseCase by inject()
    val pokemonSearchUseCase: PokemonSearchUseCase by inject()
    val pokemonLikeUseCase: PokemonLikeUseCase by inject()
    val saveSimplePokemonUseCase: SaveSimplePokemonUseCase by inject()
    val viewModelHome: HomeViewModel by inject()
    val viewModelPokedex: PokedexViewModel by inject()
}
