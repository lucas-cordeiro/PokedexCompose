package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface PokemonSearchUseCase {
    suspend fun doPokemonSearchByName(nameQuery: String, limit: Long) : Flow<Result<List<Pokemon>>>
}