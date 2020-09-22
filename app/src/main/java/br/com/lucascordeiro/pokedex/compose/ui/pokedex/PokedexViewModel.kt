package br.com.lucascordeiro.pokedex.compose.ui.pokedex

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonUseCase
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PokedexViewModel(private val useCase: GetPokemonUseCase) : ViewModel() {
    var pokemons: List<Pokemon> by mutableStateOf(listOf())
        private set

    var loading: Boolean by mutableStateOf(true)
        private set

    var scrollPosition: Float by mutableStateOf(0f)
        private set

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?>
        get() = _errorMessage

    private var loadingMoreItems = false

    fun addScrollPosition(position: Float) {
        scrollPosition = position
    }

    fun loadMoreItems(count: Long) {
        if (!loadingMoreItems) {
            viewModelScope.launch(IO) {
                loading = true
                loadingMoreItems = true
                useCase.doGetMorePokemon(count)
            }
        }
    }

    init {
        Log.d("BUG", "init")
        viewModelScope.launch {
            useCase.doGetPokemon()
                    .collect {
                        when (it) {
                            is Result.Success -> {
                                loading = false
                                loadingMoreItems = false
                                pokemons = it.data
                            }
                            is Result.Error -> {
                                when (it.error) {
                                    is ErrorEntity.ApiError.Network -> {
                                        _errorMessage.value =
                                                "Falha na conexão com a internet, verifique e tente novamente"
                                    }
                                    else ->
                                        _errorMessage.value =
                                                "Ocorreu um erro, tente novamente"
                                }
                            }
                        }
                    }
        }
    }
}
