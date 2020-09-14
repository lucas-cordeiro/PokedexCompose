package br.com.lucascordeiro.pokedex.compose.ui.pokedetail

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.lucascordeiro.pokedex.compose.di.component.PokedexComponent
import br.com.lucascordeiro.pokedex.compose.ui.home.HomeViewModel
import br.com.lucascordeiro.pokedex.compose.ui.theme.grey800
import br.com.lucascordeiro.pokedex.compose.ui.theme.typography
import br.com.lucascordeiro.pokedex.compose.ui.utils.NetworkImage
import br.com.lucascordeiro.pokedex.compose.ui.utils.SharedElement
import br.com.lucascordeiro.pokedex.compose.ui.utils.SharedElementType
import br.com.lucascordeiro.pokedex.compose.ui.utils.toPokemonTypeTheme
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import java.util.*

@Composable
fun PokemonDetail(
    pokemonBasic: Pokemon,
    upPress: () -> Unit
){
    val viewModel: PokeDetailViewModel = viewModel(null, object : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PokeDetailViewModel(PokedexComponent().useCase) as T
        }
    })
    viewModel.getPokemonId(pokemonId = pokemonBasic.id)
    val pokemon = viewModel.pokemon
    val pokemonTypeTheme = remember(pokemonBasic.id) { pokemonBasic.type.first().toPokemonTypeTheme() }
    Column(
        modifier = Modifier.fillMaxSize(1f)
    ) {
        SharedElement(tag = "${pokemonBasic.id}_Text", type = SharedElementType.TO) {
            Text(
                text =pokemonBasic.name.capitalize(Locale.getDefault()),
                style = typography.h5,
                color = pokemonTypeTheme.colorLight,
                modifier = Modifier.wrapContentWidth()
            )
        }
        SharedElement(tag = "${pokemonBasic.id}_Image", type = SharedElementType.TO) {
            NetworkImage(
                url = pokemon?.imageUrl ?: "",
                modifier = Modifier
                    .clip(CircleShape)
                    .preferredSize(200.dp)
                    .clickable(onClick = upPress),
                contentScale = ContentScale.Crop,
                placeholderColor = null
            )
        }
    }
}