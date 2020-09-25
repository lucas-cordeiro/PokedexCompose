package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.*

class PokemonDetailUseCaseImpl(
        private val pokemonRepository: PokemonRepository,
        private val errorHandler: ErrorHandler
) : PokemonDetailUseCase {
    override fun doGetPokmeonById(pokemonId: Long) = flow {
        pokemonRepository.doGetPokemonByIdFromDatabase(pokemonId).collect { pokemonFromDatabase ->
            if (pokemonFromDatabase == null) {
                pokemonRepository.doGetPokemonByIdFromNetwork(pokemonId).first()?.let { pokemon ->
                    pokemonRepository.doInsertPokemonToDatabase(pokemon)
                }
            } else {
                emit(pokemonFromDatabase)
            }
        }
    }.map {
        val data: Result<Pokemon> = Result.Success(it)
        data
    }.catch {
        it.printStackTrace()
        emit(Result.Error(errorHandler.getError(it)))
    }
}