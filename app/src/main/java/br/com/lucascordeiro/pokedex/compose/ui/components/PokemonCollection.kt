package br.com.lucascordeiro.pokedex.compose.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.ui.pokedex.generateList
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.utils.SharedElementsRoot
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import kotlin.math.ceil

@Composable
fun PokemonCollection(
    onPokemonSelected: (Pokemon) -> Unit,
    scrollPosition: () -> Float,
    setScrollPosition: (Float) -> Unit,
    pokemons: List<Pokemon>,
    loadMoreItems: () -> Unit,
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    scrollState.scrollTo(scrollPosition())

        ScrollableColumn(
            scrollState = scrollState,
            modifier = modifier.fillMaxSize()
        ) {
            StaggeredVerticalGrid(
                maxColumnWidth = 220.dp,
                modifier = Modifier.fillMaxHeight().padding(4.dp)
            ) {
                pokemons.forEach { pokemon ->
                    PokemonItem(
                        scrollState = scrollState,
                        setPosition = setScrollPosition,
                        onPokemonSelected = onPokemonSelected,
                        modifier = Modifier,
                        pokemon = pokemon
                    )
                }
            }

            Button(
                onClick = {
                    setScrollPosition(scrollState.value)
                    loadMoreItems()
                },
                modifier = Modifier
                    .padding(0.dp, 8.dp)
                    .drawOpacity(if (loading) 0f else 1f),
                backgroundColor = MaterialTheme.colors.background
            ) {
                Text(
                    text = "Carregar mais",
                    style = TextStyle(
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(4.dp)

                )
            }
        }
}

@Preview
@Composable
fun PreviewPokemonList() {
    PokedexComposeTheme(darkTheme = true) {
        SharedElementsRoot {
            Surface(contentColor = contentColor()) {
                PokemonCollection(
                    setScrollPosition = {},
                    scrollPosition = { 1f },
                    pokemons = remember { generateList() },
                    onPokemonSelected = {},
                    loadMoreItems = {},
                    loading = false
                )
            }
        }
    }
}
