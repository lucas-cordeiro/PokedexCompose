package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonSimple
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.repository.PokemonSimpleRepository
import br.com.lucascordeiro.pokedex.domain.utils.TOTAL_POKEMON_COUNT
import kotlinx.coroutines.flow.*

class GetPokemonListUseCaseImpl(
        private val pokemonRepository: PokemonRepository,
        private val pokemonSimpleRepository: PokemonSimpleRepository,
        private val errorHandler: ErrorHandler
) : GetPokemonListUseCase {
    override suspend fun getPokemons(offset: Long, limit: Long) = flow {
        val pokemonsFromNetwork = if(pokemonSimpleRepository.getPokemonsIdsFromDatabase(offset = offset, limit = limit).first().size < limit){
            pokemonSimpleRepository.bulkInsertPokemonToDatabase(pokemonRepository.getPokemonsFromNetwork(offset = 0, TOTAL_POKEMON_COUNT).first().map { pokemon ->  PokemonSimple(
                    id = pokemon.id,
                    name = pokemon.name
            ) })
            pokemonSimpleRepository.getPokemonsFromDatabase(offset = offset, limit = limit).first()
        }else{
            pokemonSimpleRepository.getPokemonsFromDatabase(offset = offset, limit = limit).first()
        }
        val pokemonsIdFromNetwork = pokemonsFromNetwork.map { it.id }
        val pokemonsIdFromDatabase =
            pokemonRepository.getPokemonsIdsFromDatabase(offset = offset, limit = limit).first()
        val needPokemonsId = pokemonsIdFromNetwork.filter { !pokemonsIdFromDatabase.contains(it) }
        val pokemonsToDatabase: MutableList<Pokemon> = ArrayList()
        needPokemonsId.forEach {
            val pokemon = pokemonRepository.getPokemonByIdFromNetwork(it).first()
            pokemon?.let {
                pokemonsToDatabase.add(it)
            }
        }
        pokemonRepository.bulkInsertPokemonToDatabase(pokemonsToDatabase)
        emitAll(
            pokemonRepository
                .getPokemonsFromDatabase(offset = offset, limit = limit)
        )
    }.map {
        val data: Result<List<Pokemon>> = Result.Success(it)
        data
    }.catch {
        it.printStackTrace()
        emit(Result.Error(errorHandler.getError(it)))
    }
}
