package br.com.lucascordeiro.pokedex.compose.ui.pokedetail

import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import br.com.lucascordeiro.pokedex.compose.di.component.PokedexComponent
import br.com.lucascordeiro.pokedex.compose.ui.Destination
import br.com.lucascordeiro.pokedex.compose.ui.MainViewModel

@Composable
fun PokemonDetail(
    pokemonId: Long,
    upPress: () -> Unit,
    viewModel: MainViewModel
){
    viewModel.getPokemonId(pokemonId = pokemonId)
    val pokemon = viewModel.pokemon
    Text(
        text = pokemon?.name ?: "null",
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.onSurface
    )
}