package br.com.lucascordeiro.pokedex.compose

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.lifecycleScope
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.ui.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.domain.model.Result
import br.com.lucascordeiro.pokedex.domain.usecase.GetPokemonUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val getPokemonUseCase by inject<GetPokemonUseCase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                getPokemonUseCase.doGetPokemon().collect {
                    when(it){
                        is Result.Success -> {
                            val bulbasaur = it.data.firstOrNull()
                            Log.d("DEBUG", "${bulbasaur?.name}(${bulbasaur?.type?.first()?.name}): ${bulbasaur?.imageUrl}")
                        }
                    }
                }
            }
        }
        setContent {
            PokedexComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PokedexComposeTheme {
        Greeting("Android")
    }
}