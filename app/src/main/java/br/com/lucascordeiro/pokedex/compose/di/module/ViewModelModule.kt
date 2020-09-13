package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.compose.ui.home.HomeViewModel
import br.com.lucascordeiro.pokedex.compose.ui.pokedetail.PokeDetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { PokeDetailViewModel(get()) }
}