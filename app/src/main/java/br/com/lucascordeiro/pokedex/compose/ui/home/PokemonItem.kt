package br.com.lucascordeiro.pokedex.compose.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.R
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.theme.grey800
import br.com.lucascordeiro.pokedex.compose.ui.theme.typography
import br.com.lucascordeiro.pokedex.compose.ui.utils.NetworkImage
import br.com.lucascordeiro.pokedex.compose.ui.utils.SharedElement
import br.com.lucascordeiro.pokedex.compose.ui.utils.SharedElementType
import br.com.lucascordeiro.pokedex.compose.ui.utils.toPokemonTypeTheme
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade
import java.util.*


@Composable
fun PokemonItem(
    scrollState: ScrollState,
    setPosition: (Float) -> Unit,
    onPokemonSelected: (Pokemon) -> Unit,
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    val pokemonTypeTheme = remember(pokemon.id) { pokemon.type.first().toPokemonTypeTheme() }
    Surface(
        modifier = modifier
            .padding(5.dp)
            .preferredWidth(220.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = {
                setPosition(scrollState.value)
                onPokemonSelected(pokemon)
            })
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                SharedElement(tag =  "${pokemon.id}_Text", type = SharedElementType.FROM) {
                    Text(
                        text = pokemon.name.capitalize(Locale.getDefault()),
                        style = typography.subtitle1,
                        color = pokemonTypeTheme.colorLight
                    )
                }
                Column {
                    pokemon.type.forEach {
                        PokemonType(type = it.type.capitalize(Locale.getDefault()))
                    }
                }
            }
            Column {
                Row(
                    modifier = modifier.preferredWidth(100.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    PokemonId(
                        id = "#${pokemon.id.toString().padStart(3, '0')}",
                        color = pokemonTypeTheme.colorLight.copy(alpha = 0.3f)
                    )
                }
                ConstraintLayout(
                    modifier = modifier.preferredWidth(100.dp)
                ) {
                    val (pokeball, image) = createRefs()
                    CoilImageWithCrossfade(
                        data = R.drawable.pokeball,
                        modifier = Modifier
                            .clip(CircleShape)
                            .preferredSize(100.dp)
                            .drawOpacity(0.2f)
                            .constrainAs(pokeball) {
                                centerTo(parent)
                            },
                        contentScale = ContentScale.Inside
                    )
                    SharedElement(tag =  "${pokemon.id}_Image", type = SharedElementType.FROM, modifier = Modifier.constrainAs(image) {
                        centerTo(parent)
                    }) {
                        NetworkImage(
                            url = pokemon.imageUrl,
                            modifier = Modifier
                                .clip(CircleShape)
                                .preferredSize(80.dp)
                            ,
                            contentScale = ContentScale.Crop,
                            placeholderColor = grey800.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonId(
    id: String,
    color: Color,
    modifier: Modifier = Modifier
){
    Surface(
        modifier = Modifier.padding(0.dp,0.dp,0.dp,4.dp)
    ) {
        Text(
            modifier = modifier,
            text = id,
            style = typography.subtitle2,
            color = color)
    }
}

@Composable
fun PokemonType(
    type: String,
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier
            .padding(0.dp,4.dp,0.dp,0.dp)
            .clip(RoundedCornerShape(10.dp)),
        color = grey800.copy(alpha = 0.4f)
    ) {
        Text(
            modifier = modifier.padding(10.dp, 2.dp),
            text = type,
            style = MaterialTheme.typography.body2
        )
    }
}

@Preview
@Composable
fun PreviewPokemon(){
    PokedexComposeTheme(darkTheme = true) {
        Surface(contentColor = contentColor()) {
            val pokemon = remember {
                Pokemon(
                    id = 4,
                    name = "Charmander",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
                    type = listOf(PokemonType.FIRE)
                )
            }
            PokemonItem(
                pokemon = pokemon,
                onPokemonSelected = {},
                scrollState = rememberScrollState(),
                setPosition = {}
            )
        }
    }
}