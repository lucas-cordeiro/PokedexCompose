package br.com.lucascordeiro.pokedex.compose.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.R
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.theme.grey800
import br.com.lucascordeiro.pokedex.compose.ui.theme.grey900
import br.com.lucascordeiro.pokedex.compose.ui.theme.typography
import br.com.lucascordeiro.pokedex.compose.ui.utils.*
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import java.util.*

@Composable
fun PokemonItem(
    scrollState: ScrollState,
    setPosition: (Float) -> Unit,
    onPokemonSelected: (Pokemon) -> Unit,
    pokemon: Pokemon,
    updateLike: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    PokemonView(
            scrollState = scrollState,
            pokemon = pokemon,
            setPosition = setPosition,
            onPokemonSelected = onPokemonSelected,
            updateLike = updateLike,
            modifier = modifier
    )
}

@Composable
fun PokemonView(
    scrollState: ScrollState,
    setPosition: (Float) -> Unit,
    onPokemonSelected: (Pokemon) -> Unit,
    updateLike: (Long, Boolean) -> Unit,
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    val pokemonTypeTheme = remember(pokemon.id) {
        pokemon.type.first().toPokemonTypeTheme()
    }

    Surface(
        color = if (MaterialTheme.colors.isLight) pokemonTypeTheme.colorLight else MaterialTheme.colors.surface,
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
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            val (pokemonName, pokemonTypes, pokemonId, pokemonImage, like) = createRefs()
            PokemonName(
                    pokemonId = pokemon.id,
                    name = pokemon.name,
                    textColor = if (MaterialTheme.colors.isLight) Color.White else pokemonTypeTheme.colorLight,
                    modifier = Modifier.constrainAs(pokemonName) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )
            Column(
                    modifier = Modifier.constrainAs(pokemonTypes) {
                        top.linkTo(pokemonName.bottom)
                        start.linkTo(parent.start)
                    }
            ) {
                pokemon.type.forEach {
                    PokemonType(type = it.type.capitalize(Locale.getDefault()))
                }
            }
            PokemonId(
                    id = "#${pokemon.id.toString().padStart(3, '0')}",
                    color = if (MaterialTheme.colors.isLight) pokemonTypeTheme.colorDark else pokemonTypeTheme.colorLight.copy(
                            alpha = 0.3f
                    ),
                    modifier = Modifier.constrainAs(pokemonId) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )
            PokemonImage(
                    pokemonId = pokemon.id,
                    pokemonImage = pokemon.imageUrl,
                    modifier = Modifier.constrainAs(pokemonImage) {
                        top.linkTo(pokemonId.bottom)
                        end.linkTo(parent.end)
                    }
            )
            PokemonLike(
                    pokemonId = pokemon.id,
                    like = pokemon.like,
                    updateLike = updateLike,
                    modifier = Modifier
                            .preferredHeight(24.dp)
                            .constrainAs(like) {
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                            }
            )
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

        val pokeballImage = vectorResource(id = R.drawable.ic_pokestop)

        Icon(
            asset = pokeballImage,
            tint = (if(MaterialTheme.colors.isLight) Color.White else Color.Black).copy(0.2f),
            modifier = Modifier
                .preferredSize(80.dp)
                .constrainAs(pokeball) {
                    centerTo(parent)
                }
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
                placeHolder = {
                    AnimatingLoading(
                        infinite = true,
                        modifier = Modifier.size(50.dp),
                        backgroundColor = null,
                    )
                }
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
    SharedElement(tag = "${pokemonId}_Text", type = SharedElementType.FROM, modifier) {
        Text(
            text = name.capitalize(Locale.getDefault()),
            style = typography.subtitle1,
            color = textColor,
        )
    }
}

@Composable
fun PokemonId(
    id: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(0.dp, 0.dp, 0.dp, 4.dp)
    ) {
        Text(
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
        color = grey800.copy(alpha = if(MaterialTheme.colors.isLight) 0.1f else 0.4f)
    ) {
        Text(
            modifier = Modifier.padding(10.dp, 2.dp),
            text = type,
            style = MaterialTheme.typography.body2,
            color = if(MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun PokemonLike(
        pokemonId: Long,
        like: Boolean,
        updateLike: (Long, Boolean) -> Unit,
        modifier: Modifier = Modifier
) {
    SharedElement(tag = "${pokemonId}_Like", type = SharedElementType.FROM, modifier) {
        IconButton(
                onClick = {
                    updateLike(pokemonId, !like)
                }
        ) {
            Icon(
                    asset = if (like) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    tint = MaterialTheme.colors.primary,
            )
        }
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
                    pokemon = remember { fakePokemon() },
                    updateLike = {_, _ -> }
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
