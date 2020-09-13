package br.com.lucascordeiro.pokedex.compose.di.component

import androidx.compose.ui.viewinterop.viewModel
import br.com.lucascordeiro.pokedex.compose.ui.MainViewModel
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonUseCase
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class PokedexComponent : KoinComponent{
    val useCase: GetPokemonUseCase by inject()
    val viewModel: MainViewModel by inject()
}