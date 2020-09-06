package br.com.lucascordeiro.pokedex

import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonUseCaseImpl
import br.com.lucascordeiro.pokedex.helper.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetPokemonUseCaseTest {

    private val pokemonRepository = FakePokemonRepository()

    private val getPokemonUseCase = GetPokemonUseCaseImpl(
        pokemonRepository = pokemonRepository,
        errorHandler = FakeErrorHandler()
    )

    @Test
    fun isValidDoGetPokemon(){
        runBlockingTest {
            getPokemonUseCase.doGetPokemon().take(1).collect {
                log("collect: $it")
                assertTrue(it is Result.Success)
                when(it){
                    is Result.Success -> {
                        assertTrue(it.data.first().name == "Bulbasaur")
                    }
                }
            }
        }
    }

    @Test
    fun isValidCacheControl(){
        runBlockingTest {
            var lastCacheUpdate = pokemonRepository.doGetLastCacheUpdate()
            assertEquals(lastCacheUpdate, 0L)

            getPokemonUseCase.doGetPokemon().first()
            lastCacheUpdate = pokemonRepository.doGetLastCacheUpdate()
            assertEquals(lastCacheUpdate, fakeTime)
        }
    }

    @Test
    fun isValidUpdateDatabase(){
        runBlockingTest {
            assertTrue(pokemonRepository.doGetPokemonFromDatabase().value.isEmpty())
            getPokemonUseCase.doGetPokemon().first()
            assertEquals(pokemonRepository.doGetPokemonFromDatabase().value, doGetFakeDataNetwork())
        }
    }

}