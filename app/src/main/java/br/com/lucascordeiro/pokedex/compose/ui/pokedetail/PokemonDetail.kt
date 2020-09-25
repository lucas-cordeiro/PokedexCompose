package br.com.lucascordeiro.pokedex.compose.ui.pokedetail
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.di.component.PokedexComponent
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.theme.typography
import br.com.lucascordeiro.pokedex.compose.ui.utils.*
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import java.util.*

@Composable
fun PokemonDetail(
    pokemonBasic: Pokemon,
    upPress: () -> Unit
) {
    val viewModel: PokemonDetailViewModel = viewModel(
            null,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return PokemonDetailViewModel(
                            PokedexComponent().getPokemonDetailUseCase,
                            PokedexComponent().updateLikePokemonUseCase
                    ) as T
                }
            }
    )
    viewModel.getPokemonId(pokemonId = pokemonBasic.id)
    val pokemon = viewModel.pokemon
    val pokemonData = if(pokemon?.id == pokemonBasic.id) pokemon else pokemonBasic
    PokemonDetailScreen(
            pokemon = pokemonData,
            updateLike = {
                viewModel.doUpdateLikePokemon(
                        pokemonId = pokemonData.id,
                        like = !pokemonData.like)
            }
    )
}

@Composable
fun PokemonDetailScreen(
        pokemon: Pokemon,
        updateLike: () -> Unit,
){
    val pokemonTypeTheme = remember(pokemon.id) { pokemon.type.first().toPokemonTypeTheme() }
    Column(
            modifier = Modifier.fillMaxSize(1f)
    ) {
        SharedElement(tag = "${pokemon.id}_Text", type = SharedElementType.TO) {
            Text(
                    text = pokemon.name.capitalize(Locale.getDefault()),
                    style = typography.h5,
                    color = pokemonTypeTheme.colorLight,
                    modifier = Modifier.wrapContentWidth()
            )
        }
        SharedElement(tag = "${pokemon.id}_Image", type = SharedElementType.TO) {
            NetworkImage(
                    url = pokemon.imageUrl,
                    modifier = Modifier
                            .preferredSize(200.dp),
                    contentScale = ContentScale.Crop,
                    placeholderColor = null
            )
        }
        SharedElement(tag = "${pokemon.id}_Like", type = SharedElementType.TO) {
            IconButton(
                    onClick = updateLike,
                    modifier = Modifier
                            .preferredHeight(40.dp)
            ) {
                Icon(
                        asset = if (pokemon.like) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        tint = MaterialTheme.colors.primary,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewPokemonDetailScreen() {
    PokedexComposeTheme(darkTheme = true) {
        SharedElementsRoot {
            PokemonDetailScreen(pokemon = Pokemon(
                    id = 4,
                    name = "Charmander",
                    imageUrl = "https://raw.githubusercontent.com/" +
                            "PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" +
                            "4.png",
                    type = listOf(PokemonType.FIRE)
            ), updateLike = {})
        }
    }
}
