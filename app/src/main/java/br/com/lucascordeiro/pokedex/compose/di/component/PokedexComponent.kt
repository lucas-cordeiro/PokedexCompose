package br.com.lucascordeiro.pokedex.compose.di.component

import br.com.lucascordeiro.pokedex.compose.ui.home.HomeViewModel
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonUseCase
import org.koin.core.KoinComponent
import org.koin.core.inject

class PokedexComponent : KoinComponent{
    val useCase: GetPokemonUseCase by inject()
    val viewModel: HomeViewModel by inject()
}