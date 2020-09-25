package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.model.Result

interface PokemonLikeUseCase {
    suspend fun doUpdateLikePokemonById(pokemonId: Long, like: Boolean) : Result<Unit>
}