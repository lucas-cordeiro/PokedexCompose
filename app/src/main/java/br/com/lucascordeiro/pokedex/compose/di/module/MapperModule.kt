package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.data.mapper.providePokemonNetworkMapper
import org.koin.dsl.module

val mapperModule = module {
    factory { providePokemonNetworkMapper() }
}