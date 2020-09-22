package br.com.lucascordeiro.pokedex.compose.ui.splash

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonUseCase
import br.com.lucascordeiro.pokedex.domain.utils.TOTAL_POKEMON_COUNT
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(private val useCase: GetPokemonUseCase) : ViewModel() {

    var progress: Float by mutableStateOf(0f)
        private set

    var openHome: Boolean by mutableStateOf(false)
        private set

    var errorMessage: String? by mutableStateOf(null)
        private set

    fun initialize(){
        errorMessage = null
        viewModelScope.launch(IO) {
            if (useCase.doNeedDownloadPokemonData()) {
                useCase.doDownloadPokemonData()
                        .collect {
                            Log.d("BUG", "Collect: $it")
                            when (it) {
                                is Result.Success -> {
                                    errorMessage = null
                                    val value = if (it.data > 0) it.data.toFloat() / TOTAL_POKEMON_COUNT else 0f
                                    if(value >= 1f){
                                        openHome = true
                                    }
                                    progress = value
                                }
                                is Result.Error -> {
                                    when (it.error) {
                                        is ErrorEntity.ApiError.Network -> {
                                            errorMessage = "Problems trying to connect to the internet, check your connection and try again"
                                        }
                                        else ->
                                            errorMessage = "Something went wrong, try again"
                                    }
                                }
                            }
                        }
            }
            else{
                delay(200)
                openHome = true
            }
        }
    }
}