package br.com.lucascordeiro.pokedex.compose.ui.home

import androidx.compose.foundation.Box
import androidx.compose.foundation.ContentGravity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.di.component.PokedexComponent
import br.com.lucascordeiro.pokedex.compose.ui.components.AnimatingLoading
import br.com.lucascordeiro.pokedex.compose.ui.components.TopBar
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.utils.SharedElementsRoot
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT

@Composable
fun Home(
    onPokemonSelected: (Pokemon) -> Unit
) {
    val viewModel: HomeViewModel = viewModel(
        null,
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeViewModel(PokedexComponent().useCase) as T
            }
        }
    )

    onActive(
        callback = {
            viewModel.initialize()
        }
    )

    HomeScreen(
        pokemons = viewModel.pokemons,
        loading = viewModel.loading,
        scrollPosition = {
            viewModel.scrollPosition
        },
        setScrollPosition = {
            viewModel.addScrollPosition(it)
        },
        onPokemonSelected = onPokemonSelected,
        loadMoreItems = {
            viewModel.loadMoreItems(DEFAULT_LIMIT)
        }
    )
}

@Composable
fun HomeScreen(
    onPokemonSelected: (Pokemon) -> Unit,
    scrollPosition: () -> Float,
    setScrollPosition: (Float) -> Unit,
    pokemons: List<Pokemon>,
    loadMoreItems: () -> Unit,
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopBar(title = "Pokedex")
        }
    ) {
        Stack {
            PokemonCollection(
                modifier = modifier,
                pokemons = pokemons,
                onPokemonSelected = onPokemonSelected,
                loadMoreItems = loadMoreItems,
                loading = loading,
                scrollPosition = scrollPosition,
                setScrollPosition = setScrollPosition
            )
            Loading(loading = loading)
        }
    }
}

@Composable
fun Loading(
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth(1f)
            .drawOpacity(if (loading) 1f else 0f)

    ) {
        val loadingRef = createRef()
        Surface(
            elevation = 1.dp,
            shape = CircleShape,
            modifier = Modifier
                .padding(10.dp)
                .wrapContentSize()
                .constrainAs(loadingRef) {
                    centerTo(parent)
                }
        ) {
            AnimatingLoading(
                infinite = true,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    PokedexComposeTheme(darkTheme = true) {
        SharedElementsRoot {
            HomeScreen(
                pokemons = remember { generateList() },
                onPokemonSelected = {},
                loading = true,
                scrollPosition = { 1f },
                setScrollPosition = {},
                loadMoreItems = {}
            )
        }
    }
}

fun generateList() = listOf(
    Pokemon(
        id = 1,
        name = "Bulbasaur",
        imageUrl = "https://raw.githubusercontent.com/" +
            "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
            "1.png",
        type = listOf(PokemonType.GRASS)
    ),
    Pokemon(
        id = 4,
        name = "Charmander",
        imageUrl = "https://raw.githubusercontent.com/" +
            "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
            "4.png",
        type = listOf(PokemonType.FIRE)
    )
)
