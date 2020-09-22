package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.utils.CACHE_DURATION
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT
import br.com.lucascordeiro.pokedex.domain.utils.TOTAL_POKEMON_COUNT
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

class GetPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val errorHandler: ErrorHandler
) : GetPokemonUseCase {
    private val currentOffset: ConflatedBroadcastChannel<Long> = ConflatedBroadcastChannel(0)
    private var currentLimit = DEFAULT_LIMIT

    override suspend fun doGetPokemon(limit: Long): Flow<Result<List<Pokemon>>> {
        currentLimit = limit

        return currentOffset.asFlow()
                .flatMapLatest { offset ->
                    flow {
                        emitAll(pokemonRepository.doGetPokemonFromDatabase(limit = offset + currentLimit, offset = 0))
                    }
                }
                .filter { it.isNotEmpty() }
                .map {
                    val data: Result<List<Pokemon>> = Result.Success(it)
                    data
                }
                .catch {
                    it.printStackTrace()
                    emit(Result.Error(errorHandler.getError(it)))
                }

    }

    override suspend fun doGetMorePokemon(limit: Long) {
        currentOffset.send(currentOffset.value + limit)
//        val dataFromNetwork = pokemonRepository.doGetPokemonFromNetwork(
//                offset = currentOffset.value,
//                limit = currentLimit
//        )
//        pokemonRepository.doInsertPokemonDatabase(dataFromNetwork)
    }

    override fun doGetPokemonById(pokemonId: Long): Flow<Result<Pokemon>> {
        return pokemonRepository
                .doGetPokemonByIdFromDatabase(pokemonId)
                .map {
                    val data: Result<Pokemon> = Result.Success(it)
                    data
                }
                .catch {
                    it.printStackTrace()
                    emit(Result.Error(errorHandler.getError(it)))
                }
    }

    override suspend fun doNeedDownloadPokemonData() = pokemonRepository.doGetNeedDownloadData()

    override suspend fun doDownloadPokemonData(): Flow<Result<Long>> {
        return try{
            if (pokemonRepository.doGetNeedDownloadData()) {
                var currentCount = pokemonRepository.doGetPokemonCount()
                val diff = TOTAL_POKEMON_COUNT - currentCount
                if (diff > 0) {//NeedDownload
                    val databaseIds = pokemonRepository.doGetPokemonIdsFromDatabase()
                    val networkIds = pokemonRepository.doGetPokemonIdsFromNetwork()
                    val needIds = networkIds.filter { networkId ->
                        !databaseIds.contains(networkId)
                    }
                    println("BUG networkIds: $networkIds")
                    println("BUG databaseIds: $databaseIds")
                    println("BUG needIds: $needIds")
                    flow<Result<Long>> {
                        emit(Result.Success(currentCount))
                        needIds.map { pokemonId ->
                            println("BUG needId pokemonId: $pokemonId")
                            val pokemon = pokemonRepository.doGetPokemonByIdFromNetwork(pokemonId)
                            println("BUG POKEMON ID: ${pokemon.id} ${pokemon.name}")
                            pokemonRepository.doInsertPokemonDatabase(listOf(pokemon))
                            currentCount+=1
                            emit(Result.Success(currentCount))
                        }
                    }.catch {
                        it.printStackTrace()
                        emit(Result.Error(errorHandler.getError(it)))
                    }
                } else {
                    pokemonRepository.doUpdateNeedDownloadData(false)
                    flowOf(Result.Success(TOTAL_POKEMON_COUNT))
                }
            } else {
                flowOf(Result.Success(TOTAL_POKEMON_COUNT))
            }
        }catch (t: Throwable){
            flowOf(Result.Error(errorHandler.getError(t)))
        }
    }
}
