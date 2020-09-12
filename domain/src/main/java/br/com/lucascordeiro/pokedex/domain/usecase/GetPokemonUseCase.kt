package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface GetPokemonUseCase {
    fun doGetPokemon() : Flow<Result<List<Pokemon>>>
    suspend fun doRefresh()
}