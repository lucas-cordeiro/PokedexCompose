package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonUseCase
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single { GetPokemonUseCaseImpl(get(), get()) as GetPokemonUseCase }
}
