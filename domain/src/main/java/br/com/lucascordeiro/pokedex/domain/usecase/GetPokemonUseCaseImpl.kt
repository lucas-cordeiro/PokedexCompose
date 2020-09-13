package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.utils.CACHE_DURATION
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

class GetPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val errorHandler: ErrorHandler
) : GetPokemonUseCase {
    override fun doGetPokemon(): Flow<Result<List<Pokemon>>> {
        return pokemonRepository
            .doGetPokemonFromDatabase()
            .map {
                Result.Success(it)
            }
            .catch {
                it.printStackTrace()
                Result.Error<ErrorEntity>(errorHandler.getError(it))
            }
    }

    override fun doGetPokemonById(pokemonId: Long): Flow<Result<Pokemon>> {
        return pokemonRepository
            .doGetPokemonByIdFromDatabase(pokemonId)
            .map {
                Result.Success(it)
            }
            .catch {
                it.printStackTrace()
                Result.Error<ErrorEntity>(errorHandler.getError(it))
            }
    }

    override suspend fun doRefresh() {
        val lastCacheUpdate = pokemonRepository.doGetLastCacheUpdate()
        val currentTime = pokemonRepository.doGetCurrentTime()
        if (currentTime - lastCacheUpdate > CACHE_DURATION) {
            val dataFromNetwork = pokemonRepository.doGetPokemonFromNetwork()
            pokemonRepository.doInsertPokemonDatabase(dataFromNetwork)
            pokemonRepository.doUpdateLastCacheUpdate(currentTime)
        }
    }
}