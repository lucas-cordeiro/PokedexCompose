package br.com.lucascordeiro.pokedex.compose.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import br.com.lucascordeiro.pokedex.domain.model.Result
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class HomeViewModel(private val useCase: GetPokemonUseCase) : ViewModel() {
    var pokemons: List<Pokemon> by mutableStateOf(listOf())
        private set

    var loading: Boolean by mutableStateOf(false)
        private set

    var scrollPosition: Float by mutableStateOf(0f)
        private set

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?>
        get() = _errorMessage

    private var loadingMoreItems = false

    fun addScrollPosition(position: Float){
        scrollPosition = position
    }

    fun loadMoreItems(count: Long){
        if(!loadingMoreItems) {
            viewModelScope.launch(IO) {
                delay(200)
                loading = true
                loadingMoreItems = true
                useCase.doGetMorePokemon(count)
            }
        }
    }

    init {
        loading = true
        viewModelScope.launch(IO) {
            useCase.doGetPokemon()
                    .collect {
                        loadingMoreItems = false
                        loading = false
                        withContext(Main) {
                            when (it) {
                                is Result.Success -> {
                                    pokemons = it.data
                                }
                                is Result.Error -> {
                                    when (it.error) {
                                        is ErrorEntity.ApiError.Network -> {
                                            _errorMessage.value = "Falha na conexão com a internet, verifique e tente novamente"
                                        }
                                        is ErrorEntity.App.Unknown -> {
                                            _errorMessage.value = "Ocorreu um erro, tente novamente"
                                        }
                                    }
                                }
                            }
                        }
                    }
        }

        viewModelScope.launch(IO) {
            useCase.doRefresh()
        }
    }
}