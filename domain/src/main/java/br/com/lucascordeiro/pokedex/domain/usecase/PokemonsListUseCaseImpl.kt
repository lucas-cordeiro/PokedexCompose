package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonSimple
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.repository.PokemonSimpleRepository
import br.com.lucascordeiro.pokedex.domain.utils.TOTAL_POKEMON_COUNT
import kotlinx.coroutines.flow.*

class PokemonListUseCaseImpl(
        private val pokemonRepository: PokemonRepository,
        private val pokemonSimpleRepository: PokemonSimpleRepository,
        private val errorHandler: ErrorHandler
) : PokemonsListUseCase {
    override suspend fun doGetPokemons(offset: Long, limit: Long) = flow {
        val pokemonsFromNetwork = if(pokemonSimpleRepository.doGetPokemonsIdsFromDatabase(offset = offset, limit = limit).first().size < limit){
            pokemonSimpleRepository.doBulkInsertPokemonToDatabase(pokemonRepository.doGetPokemonsFromNetwork(offset = 0, TOTAL_POKEMON_COUNT).first().map { pokemon ->  PokemonSimple(
                    id = pokemon.id,
                    name = pokemon.name
            ) })
            pokemonSimpleRepository.doGetPokemonsFromDatabase(offset = offset, limit = limit).first()
        }else{
            pokemonSimpleRepository.doGetPokemonsFromDatabase(offset = offset, limit = limit).first()
        }
        val pokemonsIdFromNetwork = pokemonsFromNetwork.map { it.id }
        val pokemonsIdFromDatabase =
            pokemonRepository.doGetPokemonsIdsFromDatabase(offset = offset, limit = limit).first()
        val needPokemonsId = pokemonsIdFromNetwork.filter { !pokemonsIdFromDatabase.contains(it) }
        val pokemonsToDatabase: MutableList<Pokemon> = ArrayList()
        needPokemonsId.forEach {
            val pokemon = pokemonRepository.doGetPokemonByIdFromNetwork(it).first()
            pokemon?.let {
                pokemonsToDatabase.add(it)
            }
        }
        pokemonRepository.doBulkInsertPokemonToDatabase(pokemonsToDatabase)
        emitAll(
            pokemonRepository
                .doGetPokemonsFromDatabase(offset = offset, limit = limit)
        )
    }.map {
        val data: Result<List<Pokemon>> = Result.Success(it)
        data
    }.catch {
        it.printStackTrace()
        emit(Result.Error(errorHandler.getError(it)))
    }
}
