package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.model.Result

interface UpdateLikePokemonUseCase {
    suspend fun updateLikePokemonById(pokemonId: Long, like: Boolean) : Result<Unit>
}