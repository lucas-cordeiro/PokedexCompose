package br.com.lucascordeiro.pokedex.helper

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

class FakePokemonRepository : PokemonRepository {

    private var lastCacheUpdate = 0L

    private val pokemonDatabase = MutableStateFlow<List<Pokemon>>(listOf())
    private val pokemonNetWork = MutableStateFlow(doGetFakeDataNetwork())

    override suspend fun doGetCurrentTime(): Long {
        return fakeTime
    }

    override suspend fun doGetLastCacheUpdate(): Long {
        return lastCacheUpdate
    }

    override fun doGetPokemonFromDatabase() = pokemonDatabase
    override fun doGetPokemonFromNetwork() = pokemonNetWork

    override suspend fun doInsertPokemonDatabase(list: List<Pokemon>) {
        pokemonDatabase.value = list
    }

    override suspend fun doUpdateLastCacheUpdate(time: Long) {
        lastCacheUpdate = time
    }
}

