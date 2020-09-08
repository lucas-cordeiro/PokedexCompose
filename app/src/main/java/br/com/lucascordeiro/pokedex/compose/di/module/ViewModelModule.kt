package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.compose.activity.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}