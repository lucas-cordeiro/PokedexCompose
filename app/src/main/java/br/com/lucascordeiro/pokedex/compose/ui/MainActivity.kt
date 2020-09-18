package br.com.lucascordeiro.pokedex.compose.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexComposeTheme {
                PokedexApp(backPressedDispatcher = onBackPressedDispatcher)
            }
        }
    }
}
