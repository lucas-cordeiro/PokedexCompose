package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.data.repository.PokemonRepositoryImpl
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { PokemonRepositoryImpl(get(), get()) as PokemonRepository}
}