package br.com.lucascordeiro.pokedex.domain.repository

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonSimple
import kotlinx.coroutines.flow.Flow

interface PokemonSimpleRepository {
    suspend fun doInsertPokemonToDatabase(pokemon: PokemonSimple)
    suspend fun doBulkInsertPokemonToDatabase(pokemons: List<PokemonSimple>)
    suspend fun doGetPokemonsIdsFromDatabase(offset: Long, limit: Long) : Flow<List<Long>>
    suspend fun doGetPokemonsFromDatabase(offset: Long, limit: Long) : Flow<List<PokemonSimple>>
}