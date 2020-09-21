package br.com.lucascordeiro.pokedex.compose.ui

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import br.com.lucascordeiro.pokedex.compose.ui.utils.Navigator
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * Models the screens in the app and any arguments they require.
 */
sealed class Destination : Parcelable {
    @Parcelize
    object Pokedex : Destination()

    @Parcelize
    object Home : Destination()

    @Immutable
    @Parcelize
    data class PokemonDetail(val pokemonBasic: @RawValue Pokemon) : Destination()
}

/**
 * Models the navigation actions in the app.
 */
class Actions(navigator: Navigator<Destination>) {
    val selectPokemon: (Pokemon) -> Unit = { pokemonBasic: Pokemon ->
        navigator.navigate(Destination.PokemonDetail(pokemonBasic))
    }
    val openPokedex: ()  -> Unit = {
        navigator.navigate(Destination.Pokedex)
    }
    val upPress: () -> Unit = {
        navigator.back()
    }
}
