package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT
import kotlinx.coroutines.flow.Flow

interface GetPokemonListUseCase {
    suspend fun getPokemons(offset: Long, limit: Long) : Flow<Result<List<Pokemon>>>
}