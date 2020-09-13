package br.com.lucascordeiro.pokedex.compose.ui.pokedetail

import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.lucascordeiro.pokedex.compose.di.component.PokedexComponent
import br.com.lucascordeiro.pokedex.compose.ui.home.HomeViewModel

@Composable
fun PokemonDetail(
    pokemonId: Long,
    upPress: () -> Unit
){
    val viewModel: PokeDetailViewModel = viewModel(null, object : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PokeDetailViewModel(PokedexComponent().useCase) as T
        }
    })
    viewModel.getPokemonId(pokemonId = pokemonId)
    val pokemon = viewModel.pokemon
    Text(
        text = pokemon?.name ?: "null",
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface
    )
}