package br.com.lucascordeiro.pokedex.compose.ui.home

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.di.component.PokedexComponent
import br.com.lucascordeiro.pokedex.compose.ui.components.TopBar
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.utils.DEFAULT_LIMIT


@Composable
fun Home(
    onPokemonSelected: (Pokemon) -> Unit,
    pokemons: List<Pokemon>? = null,
    modifier: Modifier = Modifier
){
    val viewModel: HomeViewModel = viewModel(null, object : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(PokedexComponent().useCase) as T
        }
    })
    Scaffold(
        topBar = {
            TopBar(title = "Pokedex")
        }
    ) {
        pokemons?.let {
            PokemonCollection(modifier = modifier,pokemons = it, onPokemonSelected = onPokemonSelected, loadMoreItems = {}, loading = false, scrollPosition = {0f}, setScrollPosition = {})
        }?: run {
            PokemonCollection(
                pokemons = viewModel.pokemons,
                loading = viewModel.loading,
                scrollPosition = {
                    viewModel.scrollPosition
                },
                setScrollPosition = {
                    viewModel.addScrollPosition(it)
                },
                onPokemonSelected = onPokemonSelected,
                loadMoreItems = {
                    viewModel.loadMoreItems(DEFAULT_LIMIT)
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewMainnScreen(){
    PokedexComposeTheme(darkTheme = true) {
        Home(pokemons = remember { generateList() }, onPokemonSelected = {})
    }
}


fun generateList() = listOf(
        Pokemon(
                id = 1,
                name = "Bulbasaur",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                type = listOf(PokemonType.GRASS)
        ),
        Pokemon(
                id = 4,
                name = "Charmander",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
                type = listOf(PokemonType.FIRE)
        )
)