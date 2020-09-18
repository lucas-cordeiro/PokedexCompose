package br.com.lucascordeiro.pokedex.domain.repository

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun doGetPokemonFromNetwork(limit: Long, offset: Long): List<Pokemon>
    fun doGetPokemonFromDatabase(limit: Long, offset: Long): Flow<List<Pokemon>>
    fun doGetPokemonByIdFromDatabase(pokemonId: Long): Flow<Pokemon>
    suspend fun doGetLastCacheUpdate(): Long
    suspend fun doGetCurrentTime(): Long
    suspend fun doUpdateLastCacheUpdate(time: Long)
    suspend fun doInsertPokemonDatabase(list: List<Pokemon>)
}