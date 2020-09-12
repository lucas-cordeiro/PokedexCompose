package br.com.lucascordeiro.pokedex.compose.activity.main

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.contentColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope.weight
import androidx.compose.foundation.layout.InnerPadding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.ui.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.typography
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.ceil


@Composable
fun MainScreen(
    pokemons: List<Pokemon>,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            TopBar(title = "Pokedex")
        }
    ) {
        PokemonList(modifier = modifier,pokemons = pokemons)
    }
}

@Preview
@Composable
fun PreviewMainnScreen(){
    PokedexComposeTheme(darkTheme = true) {
        MainScreen(pokemons = remember { generateList() })
    }
}


fun generateList() = listOf(
        Pokemon(
                id = 1,
                name = "Bulbasaur",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                type = listOf(PokemonType.GRASS)
        ),
        Pokemon(
                id = 4,
                name = "Charmander",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
                type = listOf(PokemonType.FIRE)
        )
)