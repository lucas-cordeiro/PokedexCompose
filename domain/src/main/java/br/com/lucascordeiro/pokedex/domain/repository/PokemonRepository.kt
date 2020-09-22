package br.com.lucascordeiro.pokedex.domain.repository

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun doGetPokemonFromNetwork(limit: Long, offset: Long): List<Pokemon>
    fun doGetPokemonFromDatabase(limit: Long, offset: Long): Flow<List<Pokemon>>
    fun doGetPokemonByIdFromDatabase(pokemonId: Long): Flow<Pokemon>
    suspend fun doGetPokemonByIdFromNetwork(pokemonId: Long): Pokemon
    suspend fun doGetPokemonCount(): Long
    suspend fun doGetNeedDownloadData(): Boolean
    suspend fun doUpdateNeedDownloadData(needDownloadData: Boolean)
    suspend fun doGetPokemonIdsFromDatabase(): List<Long>
    suspend fun doGetPokemonIdsFromNetwork(): List<Long>
    suspend fun doUpdateLastCacheUpdate(time: Long)
    suspend fun doInsertPokemonDatabase(list: List<Pokemon>)
}