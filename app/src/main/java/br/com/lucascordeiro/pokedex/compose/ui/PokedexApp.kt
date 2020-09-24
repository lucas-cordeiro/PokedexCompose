package br.com.lucascordeiro.pokedex.compose.ui

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import br.com.lucascordeiro.pokedex.compose.ui.home.Home
import br.com.lucascordeiro.pokedex.compose.ui.pokedex.Pokedex
import br.com.lucascordeiro.pokedex.compose.ui.pokedetail.PokemonDetail
import br.com.lucascordeiro.pokedex.compose.ui.theme.PokedexComposeTheme
import br.com.lucascordeiro.pokedex.compose.ui.utils.Navigator
import br.com.lucascordeiro.pokedex.compose.ui.utils.SharedElementsRoot

@Composable
fun PokedexApp(backPressedDispatcher: OnBackPressedDispatcher) {
    val navigator: Navigator<Destination> = rememberSavedInstanceState(
        saver = Navigator.saver<Destination>(backPressedDispatcher)
    ) {
        Navigator(Destination.Home, backPressedDispatcher)
    }

    val actions = remember(navigator) { Actions(navigator) }
    PokedexComposeTheme {
        SharedElementsRoot {
            Crossfade(
                current = navigator.current
            ) { destination ->
                when (destination) {
                    Destination.Home -> Home(
                            openPokedex = actions.openPokedex,
                            onPokemonSelected = actions.selectPokemon,
                    )
                    Destination.Pokedex -> Pokedex(
                            onPokemonSelected = actions.selectPokemon,
                            upPress = actions.upPress
                    )
                    is Destination.PokemonDetail -> {
                        PokemonDetail(
                            pokemonBasic = destination.pokemonBasic,
                            upPress = actions.upPress
                        )
                    }
                }
            }
        }
    }
}
