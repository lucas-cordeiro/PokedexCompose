package br.com.lucascordeiro.pokedex.compose.di.component

import br.com.lucascordeiro.pokedex.compose.ui.home.HomeViewModel
import br.com.lucascordeiro.pokedex.compose.ui.pokedex.PokedexViewModel
import br.com.lucascordeiro.pokedex.domain.usecase.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class PokedexComponent : KoinComponent {
    val pokemonListUseCaseGet: GetPokemonListUseCase by inject()
    val getPokemonDetailUseCase: GetPokemonDetailUseCase by inject()
    val searchPokemonUseCase: SearchPokemonUseCase by inject()
    val updateLikePokemonUseCase: UpdateLikePokemonUseCase by inject()
    val saveSimplePokemonUseCase: SaveSimplePokemonUseCase by inject()
    val viewModelHome: HomeViewModel by inject()
    val viewModelPokedex: PokedexViewModel by inject()
}
