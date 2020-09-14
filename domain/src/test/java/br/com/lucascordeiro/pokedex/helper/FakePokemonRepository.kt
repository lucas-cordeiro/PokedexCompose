package br.com.lucascordeiro.pokedex.helper

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakePokemonRepository : PokemonRepository {

    private var lastCacheUpdate = 0L

    private val pokemonDatabase = MutableStateFlow<List<Pokemon>>(listOf())
    private val pokemonNetWork = doGetFakeDataNetwork()

    override suspend fun doGetCurrentTime(): Long {
        return fakeTime
    }

    override suspend fun doGetLastCacheUpdate(): Long {
        return lastCacheUpdate
    }

    override fun doGetPokemonFromDatabase(limit: Long, offset: Long) : Flow<List<Pokemon>>{
        println("doGetPokemonFromDatabase: limit: $limit offset: $offset")
        return flowOf(pokemonDatabase.value.subList(offset.toInt(), limit.toInt()))
    }
    override suspend fun doGetPokemonFromNetwork(limit: Long, offset: Long) = pokemonNetWork.subList(offset.toInt(), (offset + limit).toInt())

    override fun doGetPokemonByIdFromDatabase(pokemonId: Long): Flow<Pokemon>  = flowOf(pokemonDatabase.value.first { it.id == pokemonId })


    override suspend fun doInsertPokemonDatabase(list: List<Pokemon>) {
        pokemonDatabase.value += list
        println("pokemonDatabase.value: ${pokemonDatabase.value} list: $list")
    }

    override suspend fun doUpdateLastCacheUpdate(time: Long) {
        lastCacheUpdate = time
    }
}

