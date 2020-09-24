package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonSimple
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.repository.PokemonSimpleRepository
import br.com.lucascordeiro.pokedex.domain.utils.TOTAL_POKEMON_COUNT
import kotlinx.coroutines.flow.*

class PokemonSearchUseCaseImpl(
        private val pokemonRepository: PokemonRepository,
        private val pokemonSimpleRepository: PokemonSimpleRepository,
        private val errorHandler: ErrorHandler
) : PokemonSearchUseCase {
    override suspend fun doPokemonSearchByName(nameQuery: String, limit: Long) = flow {
        val pokemonsFromNetwork = if(pokemonSimpleRepository.doGetPokemonsIdsFromDatabase(offset = 0, limit = TOTAL_POKEMON_COUNT).first().size < TOTAL_POKEMON_COUNT){
            pokemonSimpleRepository.doBulkInsertPokemonToDatabase(pokemonRepository.doGetPokemonsFromNetwork(0, TOTAL_POKEMON_COUNT).first().map { pokemon ->  PokemonSimple(
                    id = pokemon.id,
                    name = pokemon.name
            ) })
            pokemonSimpleRepository.doGetPokemonsFromDatabase(offset = 0, limit = TOTAL_POKEMON_COUNT).first()
        }else{
            pokemonSimpleRepository.doGetPokemonsFromDatabase(offset = 0, limit = TOTAL_POKEMON_COUNT).first()
        }
        val pokemonsSearch = pokemonsFromNetwork.filter { it.name.trim().toLowerCase().startsWith(nameQuery.toLowerCase().trim()) }
        val pokemonsIdSearch = (if(pokemonsSearch.size > 4) pokemonsSearch.subList(0,4) else pokemonsSearch).map { it.id }
        val pokemonsIdFromDatabase = pokemonRepository.doGetPokemonsIdsFromDatabase(offset = 0, limit = TOTAL_POKEMON_COUNT).first()
        val needPokemonsId = pokemonsIdSearch.filter { !pokemonsIdFromDatabase.contains(it) }
        val pokemonsToDatabase: MutableList<Pokemon> = ArrayList()
        needPokemonsId.forEach {
            val pokemon = pokemonRepository.doGetPokemonByIdFromNetwork(it).first()
            pokemon?.let {
                pokemonsToDatabase.add(it)
            }
        }
        pokemonRepository.doBulkInsertPokemonToDatabase(pokemonsToDatabase)
        emitAll(pokemonRepository.doSearchByPokemonNameFromDatabase(nameQuery.trim().toLowerCase(), limit))
    }.map {
        val data: Result<List<Pokemon>> = Result.Success(it)
        data
    }.catch {
        it.printStackTrace()
        emit(Result.Error(errorHandler.getError(it)))
    }
}