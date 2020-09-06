package br.com.lucascordeiro.pokedex.data.repository

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PokemonRepositoryImpl : PokemonRepository {
    override suspend fun doGetCurrentTime(): Long {
        return 0L
    }

    override suspend fun doGetLastCacheUpdate(): Long {
        return 0L
    }

    override suspend fun doUpdateLastCacheUpdate(time: Long) {

    }

    override fun doGetPokemonFromDatabase(): Flow<List<Pokemon>> {
        return flowOf()
    }

    override fun doGetPokemonFromNetwork(): Flow<List<Pokemon>> {
        return flowOf()
    }

    override suspend fun doInsertPokemonDatabase(list: List<Pokemon>) {

    }
}