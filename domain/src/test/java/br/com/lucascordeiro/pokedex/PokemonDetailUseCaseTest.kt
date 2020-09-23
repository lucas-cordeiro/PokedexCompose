package br.com.lucascordeiro.pokedex

import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonDetailUseCaseImpl
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonListUseCaseImpl
import br.com.lucascordeiro.pokedex.helper.FakeErrorHandler
import br.com.lucascordeiro.pokedex.helper.FakePokemonRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test

class PokemonDetailUseCaseTest {
    private val pokemonRepository = FakePokemonRepository()

    private val useCase = PokemonDetailUseCaseImpl(
            pokemonRepository = pokemonRepository,
            errorHandler = FakeErrorHandler()
    )

    @Test
    fun isValidGetPokemonDetail(){
        runBlockingTest {
            val result = useCase.doGetPokmeonById(pokemonId = 4).first()
            assertTrue(result is Result.Success)
            val pokemon = (result as Result.Success).data
            assertEquals(pokemon.name, "Charmander")
        }
    }

    @Test
    fun isValidUpdatePokemonLike(){
        runBlockingTest {
            useCase.doGetPokmeonById(pokemonId = 4).take(1).onEach {
                when(it){
                    is Result.Success->{
                        assertFalse(it.data.like)
                    }
                    else -> throw Exception("Any Error")
                }
            }.launchIn(this)
            delay(100)
            val result = useCase.doUpdateLikePokemonById(pokemonId = 4, like = true)
            assertTrue(result is Result.Success)
            useCase.doGetPokmeonById(pokemonId = 4).take(1).onEach {
                when(it){
                    is Result.Success->{
                        assertTrue(it.data.like)
                    }
                    else -> throw Exception("Any Error")
                }
            }.launchIn(this)
        }
    }
}