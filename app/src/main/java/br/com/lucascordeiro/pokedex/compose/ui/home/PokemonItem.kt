package br.com.lucascordeiro.pokedex.compose.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.R
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.theme.grey800
import br.com.lucascordeiro.pokedex.compose.ui.theme.typography
import br.com.lucascordeiro.pokedex.compose.ui.utils.*
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import dev.chrisbanes.accompanist.coil.CoilImage
import java.util.*

@Composable
fun PokemonItem(
    scrollState: ScrollState,
    setPosition: (Float) -> Unit,
    onPokemonSelected: (Pokemon) -> Unit,
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    PokemonView(
        scrollState = scrollState,
        setPosition = setPosition,
        onPokemonSelected = onPokemonSelected,
        pokemon = pokemon,
        modifier = modifier
    )
}

@Composable
fun PokemonView(
    scrollState: ScrollState,
    setPosition: (Float) -> Unit,
    onPokemonSelected: (Pokemon) -> Unit,
//        alphaProgress: () -> Float,
//        positionYProgress: () -> Float,
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    val pokemonTypeTheme = remember(pokemon.id) {
        pokemon.type.first().toPokemonTypeTheme()
    }

    Surface(
        modifier = modifier
            .padding(5.dp)
            .preferredWidth(220.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                onClick = {
                    setPosition(scrollState.value)
                    onPokemonSelected(pokemon)
                }
            )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                PokemonName(pokemonId = pokemon.id, name = pokemon.name, textColor = pokemonTypeTheme.colorLight)
                Column {
                    pokemon.type.forEach {
                        PokemonType(type = it.type.capitalize(Locale.getDefault()))
                    }
                }
            }
            Column(
                modifier = Modifier.padding(4.dp, 0.dp)
            ) {
                Row(
                    modifier = Modifier.preferredWidth(100.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    PokemonId(
                        id = "#${pokemon.id.toString().padStart(3, '0')}",
                        color = pokemonTypeTheme.colorLight.copy(alpha = 0.3f)
                    )
                }
                PokemonImage(pokemonId = pokemon.id, pokemonImage = pokemon.imageUrl)
            }
        }
    }
}

@Composable
fun PokemonImage(
    pokemonId: Long,
    pokemonImage: String,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier.width(80.dp)
    ) {
        val (pokeball, image) = createRefs()

        CoilImage(
            data = R.drawable.ic_pokestop,
            modifier = Modifier
                .preferredSize(80.dp)
                .constrainAs(pokeball) {
                    centerTo(parent)
                },
        )
        SharedElement(
            tag = "${pokemonId}_Image",
            type = SharedElementType.FROM,
            modifier = Modifier.constrainAs(image) {
                centerTo(parent)
            }
        ) {
            NetworkImage(
                url = pokemonImage,
                modifier = Modifier
                    .preferredSize(80.dp),
                contentScale = ContentScale.Crop,
                placeholderColor = null
            )
        }
    }
}

@Composable
fun PokemonName(
    pokemonId: Long,
    name: String,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    SharedElement(tag = "${pokemonId}_Text", type = SharedElementType.FROM) {
        Text(
            text = name.capitalize(Locale.getDefault()),
            style = typography.subtitle1,
            color = textColor,
            modifier = modifier
        )
    }
}

@Composable
fun PokemonId(
    id: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 4.dp)
    ) {
        Text(
            modifier = modifier,
            text = id,
            style = typography.subtitle2,
            color = color
        )
    }
}

@Composable
fun PokemonType(
    type: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(0.dp, 4.dp, 0.dp, 0.dp)
            .clip(RoundedCornerShape(10.dp)),
        color = grey800.copy(alpha = 0.4f)
    ) {
        Text(
            modifier = Modifier.padding(10.dp, 2.dp),
            text = type,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Preview
@Composable
fun PreviewPokemonView() {
    PokedexComposeTheme(darkTheme = true) {
        SharedElementsRoot {
            PokemonItem(
                scrollState = rememberScrollState(),
                setPosition = {},
                onPokemonSelected = {},
                pokemon = remember { fakePokemon() }
            )
        }
    }
}

@Preview
@Composable
fun PreviewPokemonId() {
    PokedexComposeTheme(darkTheme = true) {
        val pokemonTheme = remember {
            fakePokemon().type.first().toPokemonTypeTheme()
        }
        PokemonId(
            id = "#${fakePokemon().id.toString().padStart(3, '0')}",
            color = pokemonTheme.colorLight.copy(alpha = 0.3f)
        )
    }
}

@Preview
@Composable
fun PreviewPokemonType() {
    PokedexComposeTheme(darkTheme = true) {
        PokemonType(type = fakePokemon().type.first().name)
    }
}

private fun fakePokemon() = Pokemon(
    id = 4,
    name = "Charmander",
    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
    type = listOf(PokemonType.FIRE)
)
