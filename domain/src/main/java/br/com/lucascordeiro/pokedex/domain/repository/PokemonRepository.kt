package br.com.lucascordeiro.pokedex.domain.repository

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun searchPokemonByNameFromDatabase(nameQuery: String, limit: Long): Flow<List<Pokemon>>
    suspend fun updateLikePokemonById(pokemonId: Long, like: Boolean)
    suspend fun insertPokemonToDatabase(pokemon: Pokemon)
    suspend fun bulkInsertPokemonToDatabase(pokemons: List<Pokemon>)
    suspend fun getPokemonsIdsFromDatabase(offset: Long, limit: Long) : Flow<List<Long>>

    suspend fun getPokemonsFromDatabase(offset: Long, limit: Long) : Flow<List<Pokemon>>
    suspend fun getPokemonsFromNetwork(offset: Long, limit: Long) : Flow<List<Pokemon>>

    suspend fun getPokemonByIdFromDatabase(pokemonId: Long) : Flow<Pokemon?>
    suspend fun getPokemonByIdFromNetwork(pokemonId: Long) : Flow<Pokemon?>
}