package br.com.lucascordeiro.pokedex.compose.activity.main

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.contentColor
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope.gravity
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.ui.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.grey800
import br.com.lucascordeiro.pokedex.compose.ui.grey900
import br.com.lucascordeiro.pokedex.compose.ui.typography
import br.com.lucascordeiro.pokedex.compose.utils.NetworkImage
import br.com.lucascordeiro.pokedex.compose.utils.toPokemonTypeTheme
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import coil.Coil
import coil.util.CoilUtils
import org.koin.ext.getOrCreateScope
import java.util.*


@Composable
fun PokemonView(
    pokemon: Pokemon,
    modifier: Modifier = Modifier
){
    val pokemonTypeTheme = remember(pokemon.id) { pokemon.type.first().toPokemonTypeTheme() }
    Surface(
        modifier = modifier
            .padding(5.dp)
            .preferredWidth(220.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = {

            })
    ) {
            Row(
                modifier = modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
               Column {
                   Text(
                       text = pokemon.name.capitalize(Locale.getDefault()),
                       style = typography.subtitle1,
                       color = pokemonTypeTheme.colorLight
                   )
                   Column {
                       pokemon.type.forEach {
                           PokemonTypeView(type = it.type.capitalize(Locale.getDefault()))
                       }
                   }
               }
                Column {
                    Row(
                        modifier = modifier.preferredWidth(100.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        PokemonIdView(id = "#${pokemon.id.toString().padStart(3,'0')}", color = pokemonTypeTheme.colorLight.copy(alpha = 0.3f))
                    }
                    NetworkImage(
                        url = pokemon.imageUrl,
                        modifier = Modifier.clip(CircleShape).preferredSize(100.dp),
                        placeholderColor = grey800.copy(alpha = 0.4f)
                    )
                }
            }
    }
}

@Composable
fun PokemonIdView(
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
fun PokemonTypeView(
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
fun PreviewPokemonView(){
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
            PokemonView(pokemon = pokemon)
        }
    }
}