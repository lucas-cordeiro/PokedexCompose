package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.*

class GetPokemonDetailUseCaseImpl(
        private val pokemonRepository: PokemonRepository,
        private val errorHandler: ErrorHandler
) : GetPokemonDetailUseCase {
    override fun getPokmeonById(pokemonId: Long) = flow {
        pokemonRepository.getPokemonByIdFromDatabase(pokemonId).collect { pokemonFromDatabase ->
            if (pokemonFromDatabase == null) {
                pokemonRepository.getPokemonByIdFromNetwork(pokemonId).first()?.let { pokemon ->
                    pokemonRepository.insertPokemonToDatabase(pokemon)
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