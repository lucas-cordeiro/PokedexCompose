package br.com.lucascordeiro.pokedex

import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.SearchPokemonUseCaseImpl
import br.com.lucascordeiro.pokedex.helper.FakeErrorHandler
import br.com.lucascordeiro.pokedex.helper.FakePokemonRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchPokemonUseCaseTest {
    private val pokemonRepository = FakePokemonRepository()

    private val useCase = SearchPokemonUseCaseImpl(
            pokemonRepository = pokemonRepository,
            errorHandler = FakeErrorHandler()
    )

    @Test
    fun isValidSearch(){
        runBlockingTest {
            val result = useCase.doSearchPokemonByName("char").first()
            assertTrue(result is Result.Success)
            val pokemons = (result as Result.Success).data
            assertTrue(pokemons.size == 1)
        }
    }
}