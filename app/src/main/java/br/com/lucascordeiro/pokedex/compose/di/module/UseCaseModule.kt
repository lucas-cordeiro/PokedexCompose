package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.domain.usecase.PokemonDetailUseCase
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonDetailUseCaseImpl
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonsListUseCase
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonListUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single { PokemonListUseCaseImpl(get(), get()) as PokemonsListUseCase }
    single { PokemonDetailUseCaseImpl(get(), get()) as PokemonDetailUseCase }
}
