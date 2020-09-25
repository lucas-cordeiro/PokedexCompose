package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository

class PokemonLikeUseCaseImpl(
        private val pokemonRepository: PokemonRepository,
        private val errorHandler: ErrorHandler
) : PokemonLikeUseCase {
    override suspend fun doUpdateLikePokemonById(pokemonId: Long, like: Boolean) : Result<Unit> {
        return try{
            pokemonRepository.doUpdateLikePokemonById(pokemonId = pokemonId, like = like)
            Result.Success(Unit)
        }catch (t: Throwable){
            t.printStackTrace()
            Result.Error(errorHandler.getError(t))
        }
    }
}