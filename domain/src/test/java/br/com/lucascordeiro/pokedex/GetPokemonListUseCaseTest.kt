package br.com.lucascordeiro.pokedex

import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonListUseCaseImpl
import br.com.lucascordeiro.pokedex.helper.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

class GetPokemonListUseCaseTest {

    private val pokemonRepository = FakePokemonRepository()

    private val useCase = GetPokemonListUseCaseImpl(
        pokemonRepository = pokemonRepository,
        errorHandler = FakeErrorHandler()
    )

    @Test
    fun isValidDatabase(){
        runBlockingTest {
            assertTrue(pokemonRepository.getPokemonsFromDatabase(0,0).first().isEmpty())
            val charmander = pokemonRepository.getPokemonByIdFromNetwork(1).first()!!
            pokemonRepository.insertPokemonToDatabase(charmander)
            assertTrue(pokemonRepository.getPokemonsFromDatabase(0,0).first().size == 1)
        }
    }

    @Test
    fun isValidPokemonListUseCase(){
        runBlockingTest {
            val result = useCase.getPokemons(0,3).first()
            assertTrue(result is Result.Success)
            val pokemons = (result as Result.Success).data
            assertTrue(pokemons.size == 6)
        }
    }
}