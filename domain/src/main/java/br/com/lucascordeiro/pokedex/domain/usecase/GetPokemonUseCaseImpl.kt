package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT
import br.com.lucascordeiro.pokedex.domain.utils.TOTAL_POKEMON_COUNT
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

class GetPokemonUseCaseImpl(
    private val pokemonRepository: PokemonRepository,
    private val errorHandler: ErrorHandler
) : GetPokemonsUseCase {
    override suspend fun doGetPokemons(offset: Long, limit: Long) = flow {
        val pokemonsFromNetwork =
            pokemonRepository.doGetPokemonsFromNetwork(offset = offset, limit = limit).first()
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
        emit(Result.Error(errorHandler.getError(it)))
    }
}
