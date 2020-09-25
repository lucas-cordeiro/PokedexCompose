package br.com.lucascordeiro.pokedex.compose.ui.pokedetail

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
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
        private val pokemonDetailUseCase: PokemonDetailUseCase,
        private val pokemonLikeUseCase: PokemonLikeUseCase
) : ViewModel() {

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?>
        get() = _errorMessage

    private val _showMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val showMessage: StateFlow<String?>
        get() = _showMessage

    var pokemon: Pokemon? by mutableStateOf(null)
        private set

    fun doUpdateLikePokemon(pokemonId: Long, like: Boolean){
        viewModelScope.launch {
            when(pokemonLikeUseCase.doUpdateLikePokemonById(pokemonId = pokemonId, like = like)){
                is Result.Success -> {
                    _showMessage.value = "${pokemon?.name} Liked ❤️"
                }
                is Result.Error ->{
                    _errorMessage.value = "Ocorreu um erro, tente novamente"
                }
            }
        }
    }

    fun getPokemonId(pokemonId: Long) {
        viewModelScope.launch {
            pokemonDetailUseCase.doGetPokmeonById(pokemonId = pokemonId)
                    .flowOn(IO)
                    .collect {
                        when(it){
                            is Result.Success ->{
                                pokemon = it.data
                            }
                            is Result.Error ->{
                                when (it.error) {
                                    is ErrorEntity.ApiError.Network -> {
                                        _errorMessage.value =
                                            "Falha na conexão com a internet, verifique e tente novamente"
                                    }
                                    is ErrorEntity.App.Unknown -> {
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
