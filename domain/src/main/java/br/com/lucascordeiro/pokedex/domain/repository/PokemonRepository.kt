package br.com.lucascordeiro.pokedex.domain.repository

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun doGetPokemonFromNetwork() : List<Pokemon>
    fun doGetPokemonFromDatabase() : Flow<List<Pokemon>>
    suspend fun doGetLastCacheUpdate() : Long
    suspend fun doGetCurrentTime() : Long
    suspend fun doUpdateLastCacheUpdate(time: Long)
    suspend fun doInsertPokemonDatabase(list: List<Pokemon>)
}