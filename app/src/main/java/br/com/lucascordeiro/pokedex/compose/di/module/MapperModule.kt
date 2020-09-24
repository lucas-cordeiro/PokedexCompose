package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.data.mapper.pokemon.PokemonMapper
import br.com.lucascordeiro.pokedex.data.mapper.pokemon.PokemonMapperImpl
import br.com.lucascordeiro.pokedex.data.mapper.pokemonsimple.PokemonSimpleMapper
import br.com.lucascordeiro.pokedex.data.mapper.pokemonsimple.PokemonSimpleMapperImpl
import org.koin.dsl.module

val mapperModule = module {
    factory { PokemonMapperImpl() as PokemonMapper }
    factory { PokemonSimpleMapperImpl() as PokemonSimpleMapper }
}
