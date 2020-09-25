package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.compose.ui.home.HomeViewModel
import br.com.lucascordeiro.pokedex.compose.ui.pokedex.PokedexViewModel
import br.com.lucascordeiro.pokedex.compose.ui.pokedetail.PokemonDetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PokedexViewModel(get(),get(), get()) }
    viewModel { PokemonDetailViewModel(get(), get()) }
    viewModel { HomeViewModel(get(),get(), get(), get()) }
}
