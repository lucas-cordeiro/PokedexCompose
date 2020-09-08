package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.data.preferences.PreferenceController
import br.com.lucascordeiro.pokedex.data.preferences.PreferenceControllerImpl
import org.koin.dsl.module

val sharedPreferenceModule = module {
    single { PreferenceControllerImpl(get()) as PreferenceController }
}