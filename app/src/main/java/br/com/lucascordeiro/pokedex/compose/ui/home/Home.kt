package br.com.lucascordeiro.pokedex.compose.ui.home

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.ui.tooling.preview.Preview
import br.com.lucascordeiro.pokedex.compose.di.component.PokedexComponent
import br.com.lucascordeiro.pokedex.compose.ui.components.StaggeredVerticalGrid
import br.com.lucascordeiro.pokedex.compose.ui.pokedetail.PokemonDetailViewModel
import br.com.lucascordeiro.pokedex.compose.ui.theme.*
import androidx.compose.runtime.getValue
import br.com.lucascordeiro.pokedex.compose.ui.components.AnimatingLoading
import br.com.lucascordeiro.pokedex.compose.ui.components.PokemonCollection
import br.com.lucascordeiro.pokedex.compose.ui.components.SearchField
import br.com.lucascordeiro.pokedex.compose.ui.pokedex.generateList
import br.com.lucascordeiro.pokedex.domain.model.Pokemon

@Composable
fun Home(
        openPokedex: () -> Unit,
        onPokemonSelected: (Pokemon) -> Unit,
){
    val viewModel: HomeViewModel = viewModel(
            null,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return HomeViewModel(PokedexComponent().pokemonSearchUseCase, PokedexComponent().pokemonDetailUseCase) as T
                }
            }
    )


    HomeScreen(
            openPokedex = openPokedex,
            searchPokemon = { query -> viewModel.search(queryName = query) },
            query = viewModel.query,
            pokemons = viewModel.pokemons,
            onPokemonSelected = onPokemonSelected,
            loading = viewModel.loading,
            updateLike = { pokemonId, like ->
                viewModel.doUpdateLikePokemon(
                        pokemonId = pokemonId,
                        like = like
                )
            }
    )
}

@Composable
fun HomeScreen(
        openPokedex: () -> Unit,
        pokemons: List<Pokemon>,
        onPokemonSelected: (Pokemon) -> Unit,
        updateLike: (Long, Boolean) -> Unit,
        loading: Boolean,
        query: String,
        searchPokemon: (String) -> Unit
){
    Column {
        Surface(
                modifier = Modifier.clip(RoundedCornerShape(
                        topLeft = 0.dp,
                        topRight = 0.dp,
                        bottomLeft = 20.dp,
                        bottomRight = 20.dp
                ))
        ) {
            ConstraintLayout {
                val (title, search, progressBar, options) = createRefs()

                HomeTitle(
                        title = "What Pokemon are you looking for?",
                        modifier = Modifier
                                .padding(20.dp,0.dp)
                                .constrainAs(title){
                                    top.linkTo(parent.top, 30.dp)
                                }
                )
                SearchField(
                        value = query,
                        hint = "Search Pokemon",
                        onValueChange = searchPokemon,
                        modifier = Modifier
                                .padding(20.dp,0.dp)
                                .fillMaxWidth()
                                .constrainAs(search){

                                    top.linkTo(title.bottom, 20.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                )

                if(pokemons.isEmpty()){
                    StaggeredVerticalGrid(
                            maxColumnWidth = 220.dp,
                            modifier = Modifier
                                    .padding(4.dp)
                                    .constrainAs(options) {
                                        top.linkTo(search.bottom, 20.dp)
                                        bottom.linkTo(parent.bottom, 20.dp)
                                    }
                    ) {
                        HomeOption(title = "Pokedex", color = grassLight, onClick = { openPokedex() })
                        HomeOption(title = "Movies", color = fireLight, onClick = {})
                        HomeOption(title = "Abilities", color = waterLight, onClick = {})
                        HomeOption(title = "Items", color = electricLight, onClick = {})
                        HomeOption(title = "Locations", color = poisonLight, onClick = {})
                        HomeOption(title = "Types", color = groundLight, onClick = {})
                    }
                }
                if(loading){
                    AnimatingLoading(
                            infinite = true,
                            backgroundColor = null,
                            modifier = Modifier
                                    .preferredSize(40.dp)
                                    .constrainAs(progressBar){
                                        end.linkTo(parent.end, 24.dp)
                                        top.linkTo(search.top)
                                        bottom.linkTo(search.bottom)
                                    }
                    )
                }
            }
        }

        PokemonCollection(
                pokemons = pokemons,
                onPokemonSelected = onPokemonSelected,
                loadMoreItems = {},
                loading = true,
                scrollPosition = {0f},
                updateLike = updateLike,
                setScrollPosition = {}
        )
    }
}

@Composable
fun HomeTitle(
        title: String,
        modifier: Modifier = Modifier
){
    Text(
            text = title,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h4,
            modifier = modifier
    )
}


@Preview
@Composable
fun PreviewHomeScreen(){
    PokedexComposeTheme(darkTheme = true) {
        HomeScreen(
                query = "",
                openPokedex = {},
                searchPokemon = {_ -> },
                updateLike = {_,_ -> },
                pokemons = generateList(),
                loading = false,
                onPokemonSelected = {}
        )
    }
}