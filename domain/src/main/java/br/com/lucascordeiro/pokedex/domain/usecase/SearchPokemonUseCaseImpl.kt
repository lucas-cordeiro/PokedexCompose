package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonSimple
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.repository.PokemonSimpleRepository
import br.com.lucascordeiro.pokedex.domain.utils.TOTAL_POKEMON_COUNT
import kotlinx.coroutines.flow.*

class SearchPokemonUseCaseImpl(
        private val pokemonRepository: PokemonRepository,
        private val pokemonSimpleRepository: PokemonSimpleRepository,
        private val errorHandler: ErrorHandler
) : SearchPokemonUseCase {
    override suspend fun doSearchPokemonByName(nameQuery: String, limit: Long) = flow {
        val pokemonsFromNetwork = if(pokemonSimpleRepository.getPokemonsIdsFromDatabase(offset = 0, limit = TOTAL_POKEMON_COUNT).first().size < TOTAL_POKEMON_COUNT){
            pokemonSimpleRepository.bulkInsertPokemonToDatabase(pokemonRepository.getPokemonsFromNetwork(0, TOTAL_POKEMON_COUNT).first().map { pokemon ->  PokemonSimple(
                    id = pokemon.id,
                    name = pokemon.name
            ) })
            pokemonSimpleRepository.getPokemonsFromDatabase(offset = 0, limit = TOTAL_POKEMON_COUNT).first()
        }else{
            pokemonSimpleRepository.getPokemonsFromDatabase(offset = 0, limit = TOTAL_POKEMON_COUNT).first()
        }
        val pokemonsSearch = pokemonsFromNetwork.filter { it.name.trim().toLowerCase().startsWith(nameQuery.toLowerCase().trim()) }
        val pokemonsIdSearch = (if(pokemonsSearch.size > 4) pokemonsSearch.subList(0,4) else pokemonsSearch).map { it.id }
        val pokemonsIdFromDatabase = pokemonRepository.getPokemonsIdsFromDatabase(offset = 0, limit = TOTAL_POKEMON_COUNT).first()
        val needPokemonsId = pokemonsIdSearch.filter { !pokemonsIdFromDatabase.contains(it) }
        val pokemonsToDatabase: MutableList<Pokemon> = ArrayList()
        needPokemonsId.forEach {
            val pokemon = pokemonRepository.getPokemonByIdFromNetwork(it).first()
            pokemon?.let {
                pokemonsToDatabase.add(it)
            }
        }
        pokemonRepository.bulkInsertPokemonToDatabase(pokemonsToDatabase)
        emitAll(pokemonRepository.searchPokemonByNameFromDatabase(nameQuery.trim().toLowerCase(), limit))
    }.map {
        val data: Result<List<Pokemon>> = Result.Success(it)
        data
    }.catch {
        it.printStackTrace()
        emit(Result.Error(errorHandler.getError(it)))
    }
}