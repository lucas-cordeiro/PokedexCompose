package br.com.lucascordeiro.pokedex.helper

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.*

class FakePokemonRepository : PokemonRepository {

    private val pokemonDatabase = MutableStateFlow<List<Pokemon>>(listOf())
    private val pokemonDatabaseState: StateFlow<List<Pokemon>>
        get() = pokemonDatabase
    private val pokemonNetWork = doGetFakeDataNetwork()

    override suspend fun doGetPokemonByIdFromDatabase(pokemonId: Long) =
        flowOf(pokemonDatabaseState.value.firstOrNull { it.id == pokemonId })

    override suspend fun doGetPokemonByIdFromNetwork(pokemonId: Long) =
        flowOf(pokemonNetWork.firstOrNull { it.id == pokemonId })

    override suspend fun doGetPokemonsFromDatabase(offset: Long, limit: Long) = pokemonDatabaseState

    override suspend fun doGetPokemonsFromNetwork(offset: Long, limit: Long) = flowOf(pokemonNetWork)

    override suspend fun doGetPokemonsIdsFromDatabase(offset: Long, limit: Long) =
        flowOf(pokemonDatabaseState.value.map { it.id })

    override suspend fun doInsertPokemonToDatabase(pokemon: Pokemon) {
        val tempList = pokemonDatabase.value.toMutableList()
        tempList.add(pokemon)
        pokemonDatabase.value = tempList
    }
}