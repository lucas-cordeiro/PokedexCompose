package br.com.lucascordeiro.pokedex

import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonDetailUseCaseImpl
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonSearchUseCase
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonSearchUseCaseImpl
import br.com.lucascordeiro.pokedex.helper.FakeErrorHandler
import br.com.lucascordeiro.pokedex.helper.FakePokemonRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

class PokemonSearchUseCaseTest {
    private val pokemonRepository = FakePokemonRepository()

    private val useCase = PokemonSearchUseCaseImpl(
            pokemonRepository = pokemonRepository,
            errorHandler = FakeErrorHandler()
    )

    @Test
    fun isValidSearch(){
        runBlockingTest {
            val result = useCase.doPokemonSearchByName("char").first()
            assertTrue(result is Result.Success)
            val pokemons = (result as Result.Success).data
            assertTrue(pokemons.size == 1)
        }
    }
}