package br.com.lucascordeiro.pokedex.compose.ui.pokedex

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.di.component.PokedexComponent
import br.com.lucascordeiro.pokedex.compose.ui.components.AnimatingLoading
import br.com.lucascordeiro.pokedex.compose.ui.components.PokemonCollection
import br.com.lucascordeiro.pokedex.compose.ui.components.TopBar
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.theme.grassLight
import br.com.lucascordeiro.pokedex.compose.ui.utils.SharedElementsRoot
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT

@Composable
fun Pokedex(
    onPokemonSelected: (Pokemon) -> Unit,
    upPress: () -> Unit
) {
    val viewModel: PokedexViewModel = viewModel(
        null,
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PokedexViewModel(
                        PokedexComponent().pokemonListUseCase,
                        PokedexComponent().pokemonDetailUseCase,
                        PokedexComponent().pokemonLikeUseCase,
                ) as T
            }
        }
    )

    PokedexScreen(
        pokemons = viewModel.pokemons,
        loading = viewModel.loading,
        scrollPosition = {
            viewModel.scrollPosition
        },
        setScrollPosition = {
            viewModel.addScrollPosition(it)
        },
        updateLike = { pokemonId, like ->
            viewModel.doUpdateLikePokemon(
                pokemonId = pokemonId,
                like = like
            )
        },
        upPress = upPress,
        onPokemonSelected = onPokemonSelected,
        loadMoreItems = {
            viewModel.loadMoreItems(DEFAULT_LIMIT)
        }
    )
}

@Composable
fun PokedexScreen(
    onPokemonSelected: (Pokemon) -> Unit,
    scrollPosition: () -> Float,
    setScrollPosition: (Float) -> Unit,
    pokemons: List<Pokemon>,
    upPress: () -> Unit,
    loadMoreItems: () -> Unit,
    loading: Boolean,
    updateLike: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
            topBar = {
                TopBar(
                    title = "Pokedex",
                    tint = grassLight,
                    onBackClick = upPress
                )
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
                    updateLike = updateLike,
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
fun PreviewPokedexScreen() {
    PokedexComposeTheme(darkTheme = true) {
        SharedElementsRoot {
            PokedexScreen(
                pokemons = remember { generateList() },
                onPokemonSelected = {},
                loading = true,
                scrollPosition = { 1f },
                setScrollPosition = {},
                updateLike = { _, _ -> },
                loadMoreItems = {},
                upPress = {}
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
    ),
        Pokemon(
                id = 7,
                name = "Squirtle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "7.png",
                type = listOf(PokemonType.WATER)
        ),
        Pokemon(
                id = 10,
                name = "Caterpie",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "10.png",
                type = listOf(PokemonType.BUG)
        ),
        Pokemon(
                id = 13,
                name = "Weedle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "13.png",
                type = listOf(PokemonType.POISON)
        ),
        Pokemon(
                id = 16,
                name = "Pidgey",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "16.png",
                type = listOf(PokemonType.FIGHTING)
        ),
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
        ),
        Pokemon(
                id = 7,
                name = "Squirtle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "7.png",
                type = listOf(PokemonType.WATER)
        ),
        Pokemon(
                id = 10,
                name = "Caterpie",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "10.png",
                type = listOf(PokemonType.BUG)
        ),
        Pokemon(
                id = 13,
                name = "Weedle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "13.png",
                type = listOf(PokemonType.POISON)
        ),
        Pokemon(
                id = 16,
                name = "Pidgey",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "16.png",
                type = listOf(PokemonType.FIGHTING)
        ),
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
        ),
        Pokemon(
                id = 7,
                name = "Squirtle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "7.png",
                type = listOf(PokemonType.WATER)
        ),
        Pokemon(
                id = 10,
                name = "Caterpie",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "10.png",
                type = listOf(PokemonType.BUG)
        ),
        Pokemon(
                id = 13,
                name = "Weedle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "13.png",
                type = listOf(PokemonType.POISON)
        ),
        Pokemon(
                id = 16,
                name = "Pidgey",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "16.png",
                type = listOf(PokemonType.FIGHTING)
        ),
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
        ),
        Pokemon(
                id = 7,
                name = "Squirtle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "7.png",
                type = listOf(PokemonType.WATER)
        ),
        Pokemon(
                id = 10,
                name = "Caterpie",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "10.png",
                type = listOf(PokemonType.BUG)
        ),
        Pokemon(
                id = 13,
                name = "Weedle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "13.png",
                type = listOf(PokemonType.POISON)
        ),
        Pokemon(
                id = 16,
                name = "Pidgey",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "16.png",
                type = listOf(PokemonType.FIGHTING)
        ),
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
        ),
        Pokemon(
                id = 7,
                name = "Squirtle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "7.png",
                type = listOf(PokemonType.WATER)
        ),
        Pokemon(
                id = 10,
                name = "Caterpie",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "10.png",
                type = listOf(PokemonType.BUG)
        ),
        Pokemon(
                id = 13,
                name = "Weedle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "13.png",
                type = listOf(PokemonType.POISON)
        ),
        Pokemon(
                id = 16,
                name = "Pidgey",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "16.png",
                type = listOf(PokemonType.FIGHTING)
        ),
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
        ),
        Pokemon(
                id = 7,
                name = "Squirtle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "7.png",
                type = listOf(PokemonType.WATER)
        ),
        Pokemon(
                id = 10,
                name = "Caterpie",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "10.png",
                type = listOf(PokemonType.BUG)
        ),
        Pokemon(
                id = 13,
                name = "Weedle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "13.png",
                type = listOf(PokemonType.POISON)
        ),
        Pokemon(
                id = 16,
                name = "Pidgey",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "16.png",
                type = listOf(PokemonType.FIGHTING)
        ),
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
        ),
        Pokemon(
                id = 7,
                name = "Squirtle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "7.png",
                type = listOf(PokemonType.WATER)
        ),
        Pokemon(
                id = 10,
                name = "Caterpie",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "10.png",
                type = listOf(PokemonType.BUG)
        ),
        Pokemon(
                id = 13,
                name = "Weedle",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "13.png",
                type = listOf(PokemonType.POISON)
        ),
        Pokemon(
                id = 16,
                name = "Pidgey",
                imageUrl = "https://raw.githubusercontent.com/" +
                        "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                        "16.png",
                type = listOf(PokemonType.FIGHTING)
        )
)
