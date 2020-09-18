package br.com.lucascordeiro.pokedex

import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonUseCaseImpl
import br.com.lucascordeiro.pokedex.helper.*
import kotlinx.coroutines.flow.*
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
    fun isValidPaginationGetPokemon() {
        runBlockingTest {
            var size = 1
            getPokemonUseCase.doRefresh(limit = 1)
            getPokemonUseCase.doGetPokemon(limit = 1).take(2).onEach {
                log("collect: $it")
                assertTrue(it is Result.Success)
                when (it) {
                    is Result.Success -> {
                        log("size: ${it.data.size} correctSize: $size")
                        assertEquals(size, it.data.size)
                        size++
                    }
                }
            }.launchIn(this)
            getPokemonUseCase.doGetMorePokemon(1)
        }
    }

    @Test
    fun isValidDoGetPokemon() {
        runBlockingTest {
            getPokemonUseCase.doRefresh(limit = 3)
            getPokemonUseCase.doGetPokemon(limit = 3).take(1).collect {
                log("collect: $it")
                assertTrue(it is Result.Success)
                when (it) {
                    is Result.Success -> {
                        assertTrue(it.data.first().name == "Bulbasaur")
                    }
                }
            }
        }
    }

    @Test
    fun isValidCacheControl() {
        runBlockingTest {
            var lastCacheUpdate = pokemonRepository.doGetLastCacheUpdate()
            assertEquals(lastCacheUpdate, 0L)
            getPokemonUseCase.doRefresh(limit = 3)
            getPokemonUseCase.doGetPokemon(limit = 3).first()
            lastCacheUpdate = pokemonRepository.doGetLastCacheUpdate()
            assertEquals(lastCacheUpdate, fakeTime)
        }
    }

    @Test
    fun isValidUpdateDatabase() {
        runBlockingTest {
            assertTrue(pokemonRepository.doGetPokemonFromDatabase(limit = 0, offset = 0).first().isEmpty())
            getPokemonUseCase.doRefresh(limit = 3)
            getPokemonUseCase.doGetPokemon(limit = 3).first()
            assertEquals(pokemonRepository.doGetPokemonFromDatabase(limit = 3, offset = 0).first(), doGetFakeDataNetwork())
        }
    }
}