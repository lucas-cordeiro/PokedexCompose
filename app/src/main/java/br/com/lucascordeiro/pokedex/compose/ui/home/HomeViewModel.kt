package br.com.lucascordeiro.pokedex.compose.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonDetailUseCase
import br.com.lucascordeiro.pokedex.domain.usecase.UpdateLikePokemonUseCase
import br.com.lucascordeiro.pokedex.domain.usecase.SearchPokemonUseCase
import br.com.lucascordeiro.pokedex.domain.usecase.SaveSimplePokemonUseCase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
        private val searchPokemonUseCase: SearchPokemonUseCase,
        private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
        private val updateLikePokemonUseCase: UpdateLikePokemonUseCase,
        private val saveSimplePokemonUseCase: SaveSimplePokemonUseCase
) : ViewModel() {

    private var jobSearch: Job? = null

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?>
        get() = _errorMessage

    private val _showMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val showMessage: StateFlow<String?>
        get() = _showMessage

    var loading: Boolean by mutableStateOf(false)
        private set

    private val _query: MutableStateFlow<String> = MutableStateFlow("")
    var query: String  by mutableStateOf("")
        private set

    var pokemons: List<Pokemon> by mutableStateOf(listOf())
        private set

    fun search(queryName: String) {
        query = queryName
        _query.value = queryName
    }

    fun doUpdateLikePokemon(pokemonId: Long, like: Boolean){
        viewModelScope.launch {
            when(updateLikePokemonUseCase.updateLikePokemonById(pokemonId = pokemonId, like = like)){
                is Result.Success -> {
                    _showMessage.value = "${pokemons.find { it.id == pokemonId }?.name} Liked ❤️"
                }
                is Result.Error ->{
                    _errorMessage.value = "Ocorreu um erro, tente novamente"
                }
            }
        }
    }

    init {
        viewModelScope.launch(IO) {
            saveSimplePokemonUseCase.doSaveSimplePokemon()
        }
        viewModelScope.launch {
            _query
                    .buffer()
                    .filter {
                        val notBlank = it.isNotBlank()
                        if(!notBlank){
                            loading = false
                            jobSearch?.cancel()
                            pokemons = emptyList()
                        }
                        notBlank
                    }
                    .debounce(500)
                    .collect {query->
                        jobSearch?.cancel()
                        if(this@HomeViewModel.query.isNotBlank()) {
                            loading = true
                            jobSearch = viewModelScope.launch {
                                searchPokemonUseCase
                                        .doSearchPokemonByName(query, limit = 4)
                                        .flowOn(IO)
                                        .collect {
                                            loading = false
                                            when (it) {
                                                is Result.Success -> {
                                                    pokemons = if (this@HomeViewModel.query.isNotBlank())
                                                        it.data
                                                    else
                                                        emptyList()
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
        }
    }
}