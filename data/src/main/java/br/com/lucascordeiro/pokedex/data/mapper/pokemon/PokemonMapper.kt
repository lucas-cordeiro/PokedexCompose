package br.com.lucascordeiro.pokedex.data.mapper.pokemon

import br.com.lucascordeiro.pokedex.data.database.entity.PokemonEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonTypeEntity
import br.com.lucascordeiro.pokedex.data.mapper.Mapper
import br.com.lucascordeiro.pokedex.data.network.model.PokemonNetwork
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType

interface PokemonMapper {
    fun providePokemonNetworkMapper(): Mapper<PokemonNetwork, Pokemon>

    fun providePokemonEntityToPokemonMapper(): Mapper<PokemonEntity, Pokemon>

    fun providePokemonToPokemonEntityMapper() : Mapper<Pokemon, PokemonEntity>

    fun providePokemonTypeToPokemonTypeEntityMapper() : Mapper<PokemonType, PokemonTypeEntity>

    fun providePokemonTypeEntityToPokemonTypeMapper() : Mapper<PokemonTypeEntity, PokemonType>
}