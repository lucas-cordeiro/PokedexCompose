package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT
import kotlinx.coroutines.flow.Flow

interface GetPokemonUseCase {
    suspend fun doGetPokemon(limit: Long = DEFAULT_LIMIT): Flow<Result<List<Pokemon>>>
    suspend fun doGetMorePokemon(limit: Long= DEFAULT_LIMIT)
    fun doGetPokemonById(pokemonId: Long): Flow<Result<Pokemon>>
    suspend fun doDownloadPokemonData() : Flow<Result<Long>>
    suspend fun doNeedDownloadPokemonData() : Boolean
}