package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository

class UpdateLikePokemonUseCaseImpl(
        private val pokemonRepository: PokemonRepository,
        private val errorHandler: ErrorHandler
) : UpdateLikePokemonUseCase {
    override suspend fun updateLikePokemonById(pokemonId: Long, like: Boolean) : Result<Unit> {
        return try{
            pokemonRepository.updateLikePokemonById(pokemonId = pokemonId, like = like)
            Result.Success(Unit)
        }catch (t: Throwable){
            t.printStackTrace()
            Result.Error(errorHandler.getError(t))
        }
    }
}