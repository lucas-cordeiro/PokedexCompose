package br.com.lucascordeiro.pokedex.data.mapper.pokemon

import br.com.lucascordeiro.pokedex.data.database.entity.PokemonEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonTypeEntity
import br.com.lucascordeiro.pokedex.data.mapper.Mapper
import br.com.lucascordeiro.pokedex.data.network.model.PokemonNetwork
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType

class PokemonMapperImpl : PokemonMapper {
    override fun providePokemonNetworkMapper() = object : Mapper<PokemonNetwork, Pokemon> {
        override fun map(input: PokemonNetwork): Pokemon {
            return Pokemon(
                id = input.id?:0L,
                name = input.name?:"",
                type = listOf(PokemonType.NORMAL),
                imageUrl = ""
            )
        }
    }

    override fun providePokemonEntityToPokemonMapper() = object : Mapper<PokemonEntity, Pokemon> {
        override fun map(input: PokemonEntity): Pokemon {
            return Pokemon(
                id = input.pokemonId?:0L,
                name = input.name?:"",
                type = input.types?.map { providePokemonTypeEntityToPokemonTypeMapper().map(it) }.orEmpty(),
                imageUrl = input.imageUrl?:""
            )
        }
    }

    override fun providePokemonToPokemonEntityMapper() = object : Mapper<Pokemon, PokemonEntity> {
        override fun map(input: Pokemon): PokemonEntity {
            return PokemonEntity(
                pokemonId = input.id,
                name = input.name,
                imageUrl = input.imageUrl
            )
        }
    }

    override fun providePokemonTypeToPokemonTypeEntityMapper() = object : Mapper<PokemonType, PokemonTypeEntity> {
        override fun map(input: PokemonType): PokemonTypeEntity {
            return PokemonTypeEntity(name = input.name)
        }
    }

    override fun providePokemonTypeEntityToPokemonTypeMapper() = object : Mapper<PokemonTypeEntity, PokemonType> {
        override fun map(input: PokemonTypeEntity): PokemonType {
            return PokemonType.valueOf(input.name?.toUpperCase()?:PokemonType.NORMAL.name)
        }
    }
}

