package br.com.lucascordeiro.pokedex.domain.repository

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonSimple
import kotlinx.coroutines.flow.Flow

interface PokemonSimpleRepository {
    suspend fun insertPokemonToDatabase(pokemon: PokemonSimple)
    suspend fun bulkInsertPokemonToDatabase(pokemons: List<PokemonSimple>)
    suspend fun getPokemonsIdsFromDatabase(offset: Long, limit: Long) : Flow<List<Long>>
    suspend fun getPokemonsFromDatabase(offset: Long, limit: Long) : Flow<List<PokemonSimple>>
}