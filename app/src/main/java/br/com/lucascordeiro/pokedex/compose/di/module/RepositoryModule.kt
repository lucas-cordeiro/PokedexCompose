package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.data.repository.PokemonRepositoryImpl
import br.com.lucascordeiro.pokedex.data.repository.PokemonSimpleRepositoryImpl
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.repository.PokemonSimpleRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { PokemonRepositoryImpl(get(), get(), get(), get(), get()) as PokemonRepository }
    single { PokemonSimpleRepositoryImpl(get(),get()) as PokemonSimpleRepository}
}
