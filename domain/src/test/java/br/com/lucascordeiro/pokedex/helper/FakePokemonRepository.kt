package br.com.lucascordeiro.pokedex.helper

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.*

class FakePokemonRepository : PokemonRepository {

    private val pokemonDatabase = MutableStateFlow<List<Pokemon>>(listOf())
    private val pokemonDatabaseState: StateFlow<List<Pokemon>>
        get() = pokemonDatabase
    private val pokemonNetWork = doGetFakeDataNetwork()

    override suspend fun getPokemonByIdFromDatabase(pokemonId: Long) =
            pokemonDatabaseState.map { it.firstOrNull { it.id == pokemonId } }

    override suspend fun getPokemonByIdFromNetwork(pokemonId: Long) =
        flowOf(pokemonNetWork.firstOrNull { it.id == pokemonId })

    override suspend fun getPokemonsFromDatabase(offset: Long, limit: Long) = pokemonDatabaseState

    override suspend fun getPokemonsFromNetwork(offset: Long, limit: Long) = flowOf(pokemonNetWork)

    override suspend fun getPokemonsIdsFromDatabase(offset: Long, limit: Long) =
        flowOf(pokemonDatabaseState.value.map { it.id })

    override suspend fun insertPokemonToDatabase(pokemon: Pokemon) {
        val tempList = pokemonDatabase.value.toMutableList()
        tempList.add(pokemon)
        pokemonDatabase.value = tempList
    }

    override suspend fun bulkInsertPokemonToDatabase(pokemons: List<Pokemon>) {
        val tempList = pokemonDatabase.value.toMutableList()
        tempList.addAll(pokemons)
        pokemonDatabase.value = tempList
    }

    override suspend fun updateLikePokemonById(pokemonId: Long, like: Boolean) {
        val tempList = pokemonDatabase.value.toMutableList()
        tempList[tempList.indexOfFirst { it.id == pokemonId }].like = like
        pokemonDatabase.value =tempList
    }

    override suspend fun doSearchByPokemonNameFromDatabase(nameQuery: String) = pokemonDatabaseState.filter { pokemons ->
        pokemons.filter { pokemon ->
            pokemon.name.toLowerCase().startsWith(nameQuery)
        }.isNotEmpty()
    }
}