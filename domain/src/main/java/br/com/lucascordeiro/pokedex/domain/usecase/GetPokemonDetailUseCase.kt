package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface GetPokemonDetailUseCase {
    fun getPokmeonById(pokemonId: Long): Flow<Result<Pokemon>>
}