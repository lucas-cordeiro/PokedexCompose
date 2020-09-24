package br.com.lucascordeiro.pokedex.data.mapper.pokemonsimple

import br.com.lucascordeiro.pokedex.data.database.entity.PokemonEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonSimpleEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonTypeEntity
import br.com.lucascordeiro.pokedex.data.mapper.Mapper
import br.com.lucascordeiro.pokedex.data.network.model.PokemonNetwork
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonSimple
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import java.util.*

class PokemonSimpleMapperImpl : PokemonSimpleMapper{
    override fun providePokemonEntityToPokemonMapper() = object : Mapper<PokemonSimpleEntity?, PokemonSimple?> {
        override fun map(input: PokemonSimpleEntity?): PokemonSimple? {
            return if (input != null) {
                PokemonSimple(
                        id = input.pokemonId,
                        name = input.name.capitalize(Locale.getDefault()),
                )
            } else null
        }
    }

    override fun providePokemonToPokemonEntityMapper() = object : Mapper<PokemonSimple, PokemonSimpleEntity> {
        override fun map(input: PokemonSimple): PokemonSimpleEntity {
            return PokemonSimpleEntity(
                    pokemonId = input.id,
                    name = input.name.toLowerCase(),
            )
        }
    }
}