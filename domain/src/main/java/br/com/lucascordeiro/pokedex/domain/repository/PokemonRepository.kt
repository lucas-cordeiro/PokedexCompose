package br.com.lucascordeiro.pokedex.domain.repository

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun doInsertPokemonToDatabase(pokemon: Pokemon)
    suspend fun doBulkInsertPokemonToDatabase(pokemons: List<Pokemon>)
    suspend fun doGetPokemonsIdsFromDatabase(offset: Long, limit: Long) : Flow<List<Long>>

    suspend fun doGetPokemonsFromDatabase(offset: Long, limit: Long) : Flow<List<Pokemon>>
    suspend fun doGetPokemonsFromNetwork(offset: Long, limit: Long) : Flow<List<Pokemon>>

    suspend fun doGetPokemonByIdFromDatabase(pokemonId: Long) : Flow<Pokemon?>
    suspend fun doGetPokemonByIdFromNetwork(pokemonId: Long) : Flow<Pokemon?>
}