package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.compose.ui.pokedex.PokedexViewModel
import br.com.lucascordeiro.pokedex.compose.ui.pokedetail.PokeDetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PokedexViewModel(get()) }
    viewModel { PokeDetailViewModel(get()) }
}
