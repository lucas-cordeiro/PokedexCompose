package br.com.lucascordeiro.pokedex.compose.ui

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import br.com.lucascordeiro.pokedex.compose.ui.utils.Navigator
import kotlinx.android.parcel.Parcelize

/**
 * Models the screens in the app and any arguments they require.
 */
sealed class Destination : Parcelable {
    @Parcelize
    object Home : Destination()

    @Immutable
    @Parcelize
    data class PokemonDetail(val pokemonId: Long) : Destination()
}


/**
 * Models the navigation actions in the app.
 */
class Actions(navigator: Navigator<Destination>) {
    val selectPokemon: (Long) -> Unit = { pokemonId: Long ->
        navigator.navigate(Destination.PokemonDetail(pokemonId))
    }
    val upPress: () -> Unit = {
        navigator.back()
    }
}