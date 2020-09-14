package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.utils.CACHE_DURATION
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class GetPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val errorHandler: ErrorHandler
) : GetPokemonUseCase {
    private val currentOffset: ConflatedBroadcastChannel<Long> = ConflatedBroadcastChannel(0)
    private var currentLimit = DEFAULT_LIMIT

    override suspend fun doGetPokemon(limit: Long): Flow<Result<List<Pokemon>>> {
        currentLimit = limit

        return currentOffset.asFlow()
            .flatMapLatest {offset ->
                flow {
                    emitAll(pokemonRepository.doGetPokemonFromDatabase(limit = offset + currentLimit, offset = 0))
                }
            }
            .map {
                Result.Success(it)
            }
            .catch {
                it.printStackTrace()
                Result.Error<ErrorEntity>(errorHandler.getError(it))
            }
    }

    override suspend fun doGetMorePokemon(limit: Long) {
        val dataFromNetwork = pokemonRepository.doGetPokemonFromNetwork(
            offset = currentOffset.value + limit,
            limit = currentLimit
        )
        pokemonRepository.doInsertPokemonDatabase(dataFromNetwork)
        currentOffset.send(currentOffset.value + limit)
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

    override suspend fun doRefresh(offset: Long, limit: Long) {
        val lastCacheUpdate = pokemonRepository.doGetLastCacheUpdate()
        val currentTime = pokemonRepository.doGetCurrentTime()
        if (currentTime - lastCacheUpdate > CACHE_DURATION) {
            val dataFromNetwork = pokemonRepository.doGetPokemonFromNetwork(
                offset = offset,
                limit = limit
            )
            pokemonRepository.doInsertPokemonDatabase(dataFromNetwork)
            pokemonRepository.doUpdateLastCacheUpdate(currentTime)
        }
    }
}