package br.com.lucascordeiro.pokedex

import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonListUseCaseImpl
import br.com.lucascordeiro.pokedex.helper.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

class PokemonsListUseCaseTest {

    private val pokemonRepository = FakePokemonRepository()

    private val useCase = PokemonListUseCaseImpl(
        pokemonRepository = pokemonRepository,
        errorHandler = FakeErrorHandler()
    )

    @Test
    fun isValidDatabase(){
        runBlockingTest {
            assertTrue(pokemonRepository.doGetPokemonsFromDatabase(0,0).first().isEmpty())
            val charmander = pokemonRepository.doGetPokemonByIdFromNetwork(1).first()!!
            pokemonRepository.doInsertPokemonToDatabase(charmander)
            assertTrue(pokemonRepository.doGetPokemonsFromDatabase(0,0).first().size == 1)
        }
    }

    @Test
    fun isValidPokemonListUseCase(){
        runBlockingTest {
            val result = useCase.doGetPokemons(0,3).first()
            assertTrue(result is Result.Success)
            val pokemons = (result as Result.Success).data
            assertTrue(pokemons.size == 6)
        }
    }
}