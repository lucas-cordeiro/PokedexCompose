package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface SearchPokemonUseCase {
    suspend fun doSearchPokemonByName(nameQuery: String, limit: Long) : Flow<Result<List<Pokemon>>>
}