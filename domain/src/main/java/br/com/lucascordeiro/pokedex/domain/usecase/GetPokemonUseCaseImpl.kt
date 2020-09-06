package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.utils.CACHE_DURATION
import kotlinx.coroutines.flow.*

class GetPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val errorHandler: ErrorHandler
) : GetPokemonUseCase{
    override fun doGetPokemon(): Flow<Result<List<Pokemon>>> {
        return pokemonRepository
            .doGetPokemonFromDatabase()
            .onStart {
                val lastCacheUpdate = pokemonRepository.doGetLastCacheUpdate()
                val currentTime = pokemonRepository.doGetCurrentTime()
                if(currentTime - lastCacheUpdate > CACHE_DURATION){
                    val dataFromNetwork = pokemonRepository.doGetPokemonFromNetwork().firstOrNull()
                    dataFromNetwork?.let {
                        pokemonRepository.doInsertPokemonDatabase(it)
                    }
                    pokemonRepository.doUpdateLastCacheUpdate(currentTime)
                }
            }
            .map {
                Result.Success(it)
            }
            .catch {
                Result.Error<ErrorEntity>(errorHandler.getError(it))
            }

    }
}