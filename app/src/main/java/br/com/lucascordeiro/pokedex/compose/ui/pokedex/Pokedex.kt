package br.com.lucascordeiro.pokedex.compose.ui.pokedex

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Backspace
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.di.component.PokedexComponent
import br.com.lucascordeiro.pokedex.compose.ui.components.*
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.theme.grassLight
import br.com.lucascordeiro.pokedex.compose.ui.theme.grey800
import br.com.lucascordeiro.pokedex.compose.ui.utils.SharedElementsRoot
import br.com.lucascordeiro.pokedex.compose.ui.utils.toPokemonTypeTheme
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT
import java.util.*

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
                            PokedexComponent().pokemonListUseCaseGet,
                            PokedexComponent().getPokemonDetailUseCase,
                            PokedexComponent().updateLikePokemonUseCase,
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
                viewModel.updateLikePokemon(
                        pokemonId = pokemonId,
                        like = like
                )
            },
            upPress = upPress,
            onPokemonSelected = onPokemonSelected,
            loadMoreItems = {
                viewModel.loadMoreItems(DEFAULT_LIMIT)
            },
            updateTypeChip = { viewModel.updateTypeChip(it) },
            typesChips = viewModel.typesChips,
            resetTypeChips = { viewModel.resetTypesChips() }
    )
}

@Composable
fun PokedexScreen(
    onPokemonSelected: (Pokemon) -> Unit,
    scrollPosition: () -> Float,
    setScrollPosition: (Float) -> Unit,
    pokemons: List<Pokemon>,
    typesChips: List<Pair<PokemonType,Boolean>>,
    updateTypeChip: (Pair<PokemonType,Boolean>) -> Unit,
    resetTypeChips: () -> Unit,
    upPress: () -> Unit,
    loadMoreItems: () -> Unit,
    loading: Boolean,
    updateLike: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val (pokedexState, setPokedexState) = remember { mutableStateOf<PokedexState>(PokedexState.PokemonList) }
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopBar(
                    title = when (pokedexState) {
                        is PokedexState.PokemonList -> "Pokedex"
                        is PokedexState.Filter -> "Filter"
                    },
                    tint = grassLight,
                    filter = true,
                    onFilterMode = pokedexState == PokedexState.Filter,
                    applyFilter = {
                        setPokedexState(PokedexState.PokemonList)
                    },
                    cancelFilter = {
                        resetTypeChips()
                        setPokedexState(PokedexState.PokemonList)
                    },
                    openFilter = {
                        setScrollPosition(scrollState.value)
                        setPokedexState(PokedexState.Filter)
                    },
                    onBackClick = upPress
            )
        }
    ) {
        Stack {
            Crossfade(current = pokedexState) {
                when(pokedexState){
                    is PokedexState.Filter -> {
                        PokedexFilter(
                                modifier = Modifier.fillMaxSize(),
                                typesChips = typesChips,
                                updateTypeChip = updateTypeChip,
                                resetTypeChips = resetTypeChips
                        )
                    }
                    is PokedexState.PokemonList -> {
                        PokemonCollection(
                                modifier = modifier,
                                pokemons = pokemons,
                                scrollState = scrollState,
                                onPokemonSelected = onPokemonSelected,
                                loadMoreItems = loadMoreItems,
                                loading = loading,
                                scrollPosition = scrollPosition,
                                updateLike = updateLike,
                                setScrollPosition = setScrollPosition
                        )
                        if (loading)
                            Loading()
                    }
                }
            }
        }
    }
}

@Composable
fun Loading(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth(1f)
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

@Composable
fun PokedexFilter(
        typesChips: List<Pair<PokemonType,Boolean>>,
        updateTypeChip: (Pair<PokemonType,Boolean>) -> Unit,
        resetTypeChips: () -> Unit,
        modifier: Modifier = Modifier
) {
    val typesChipsChecked = typesChips.count { it.second }
    Column(
            modifier = modifier.padding(10.dp)
    ) {
        ConstraintLayout(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                                top = 10.dp
                        )
        ) {
            val (typeLabel, iconClearTypes) = createRefs()
            Text(
                    text = if (typesChipsChecked > 0) "Types ($typesChipsChecked)" else "Types",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.constrainAs(typeLabel){
                        start.linkTo(parent.start)
                    }
            )
            if (typesChipsChecked > 0)
            Icon(
                    asset = Icons.Rounded.Backspace,
                    tint = MaterialTheme.colors.error,
                    modifier = Modifier
                            .padding(
                                    start = 10.dp
                            )
                            .clickable(onClick = resetTypeChips)
                            .constrainAs(iconClearTypes){
                                end.linkTo(parent.end)
                                top.linkTo(typeLabel.top)
                                bottom.linkTo(typeLabel.bottom)
                            }
            )
        }
        ScrollableRow(
                modifier = Modifier
                        .padding(
                                top = 10.dp
                        )

        ) {
            StaggeredGrid(
                    rows = 5
            ) {
                typesChips.forEach { typeChip ->
                    val type = typeChip.first
                    val checked = typeChip.second
                    val typeTheme = remember(type) { type.toPokemonTypeTheme() }
                    PokemonTypeChip(
                            type = type.type.capitalize(Locale.getDefault()),
                            textColor = if (checked) typeTheme.colorLight else if (MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.onSurface,
                            onClick = {
                                updateTypeChip(typeChip)
                            },
                            modifier = Modifier
                                    .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonTypeChip(
        type: String,
        textColor: Color = if(MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.onSurface,
        onClick: () -> Unit = {},
        modifier: Modifier = Modifier
) {
    Surface(
            modifier = modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onClick),
            color = grey800.copy(alpha = if (MaterialTheme.colors.isLight) 0.1f else 0.4f)
    ) {
        Text(
                modifier = Modifier.padding(14.dp, 4.dp),
                text = type,
                fontSize = TextUnit.Sp(14),
                fontWeight = FontWeight.Normal,
                color = textColor
        )
    }
}

@Preview
@Composable
fun PreviewPokemonType() {
    PokedexComposeTheme(darkTheme = true) {
        PokemonTypeChip(
                type = fakePokemon().type.first().name,
                textColor = fakePokemon().type.first().toPokemonTypeTheme().colorLight
        )
    }
}

private fun fakePokemon() = Pokemon(
        id = 4,
        name = "Charmander",
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
        type = listOf(br.com.lucascordeiro.pokedex.domain.model.PokemonType.FIRE)
)

@Preview
@Composable
fun PreviewPokedexScreen() {
    PokedexComposeTheme(darkTheme = true) {
        SharedElementsRoot {
            PokedexScreen(
                    pokemons = remember { generateList() },
                    onPokemonSelected = {},
                    loading = true,
                    updateTypeChip = {},
                    typesChips = emptyList(),
                    resetTypeChips = {},
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
