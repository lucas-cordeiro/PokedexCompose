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
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonDetailUseCase
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonLikeUseCase
import br.com.lucascordeiro.pokedex.domain.usecase.PokemonsListUseCase
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

class PokedexViewModel(
        private val pokemonsListUseCase: PokemonsListUseCase,
        private val pokemonDetailUseCase: PokemonDetailUseCase,
        private val pokemonLikeUseCase: PokemonLikeUseCase
) : ViewModel() {

    private var currentLimit = DEFAULT_LIMIT
    private var currentJob: Job? = null

    var pokemons: List<Pokemon> by mutableStateOf(listOf())
        private set

    var loading: Boolean by mutableStateOf(true)
        private set

    var scrollPosition: Float by mutableStateOf(0f)
        private set

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?>
        get() = _errorMessage

    private val _showMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val showMessage: StateFlow<String?>
        get() = _showMessage

    private var loadingMoreItems = false

    fun addScrollPosition(position: Float) {
        scrollPosition = position
    }

    fun loadMoreItems(count: Long) {
        if (!loadingMoreItems) {
            viewModelScope.launch(IO) {
                loading = true
                loadingMoreItems = true
                currentLimit += count
                loadData()
            }
        }
    }

    fun doUpdateLikePokemon(pokemonId: Long, like: Boolean){
        viewModelScope.launch {
            when(pokemonLikeUseCase.doUpdateLikePokemonById(pokemonId = pokemonId, like = like)){
                is Result.Success -> {
                    _showMessage.value = "${pokemons.find { it.id == pokemonId }?.name} Liked ❤️"
                }
                is Result.Error ->{
                    _errorMessage.value = "Ocorreu um erro, tente novamente"
                }
            }
        }
    }

    private fun loadData(){
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            pokemonsListUseCase.doGetPokemons(offset = 0, limit = currentLimit)
                .flowOn(IO)
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

    init {
        loadData()
    }
}
