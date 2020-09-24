package br.com.lucascordeiro.pokedex.data.mapper.pokemonsimple

import br.com.lucascordeiro.pokedex.data.database.entity.PokemonEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonSimpleEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonTypeEntity
import br.com.lucascordeiro.pokedex.data.mapper.Mapper
import br.com.lucascordeiro.pokedex.data.network.model.PokemonNetwork
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonSimple
import br.com.lucascordeiro.pokedex.domain.model.PokemonType

interface PokemonSimpleMapper {

    fun providePokemonEntityToPokemonMapper(): Mapper<PokemonSimpleEntity?, PokemonSimple?>

    fun providePokemonToPokemonEntityMapper(): Mapper<PokemonSimple, PokemonSimpleEntity>
}