package br.com.lucascordeiro.pokedex.compose.ui.pokedex

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonDetailUseCase
import br.com.lucascordeiro.pokedex.domain.usecase.UpdateLikePokemonUseCase
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonListUseCase
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

class PokedexViewModel(
        private val getPokemonListUseCase: GetPokemonListUseCase,
        private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
        private val updateLikePokemonUseCase: UpdateLikePokemonUseCase
) : ViewModel() {

    private var currentLimit = DEFAULT_LIMIT
    private var currentJob: Job? = null

    var typesChips: List<TypeChip> by mutableStateOf(generateTypeChips())
        private set

    private fun generateTypeChips() =  PokemonType.values().map { TypeChip(it, false) }

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

    fun updateTypeChip(typeChip: TypeChip){
        typesChips[typesChips.indexOf(typeChip)].checked = !typeChip.checked
    }

    fun resetTypesChips(){
        typesChips = generateTypeChips()
    }

    fun updateLikePokemon(pokemonId: Long, like: Boolean){
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

    private fun loadData(){
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            getPokemonListUseCase.getPokemons(offset = 0, limit = currentLimit)
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
