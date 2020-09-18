package br.com.lucascordeiro.pokedex.compose.ui.utils

import android.content.Context
import android.util.TypedValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import br.com.lucascordeiro.pokedex.compose.helper.PokemonTypeTheme
import br.com.lucascordeiro.pokedex.compose.ui.theme.*
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.utils.*

fun PokemonType.toPokemonTypeTheme(): PokemonTypeTheme {

    return object : PokemonTypeTheme {
        override var colorLight = this@toPokemonTypeTheme.getLightColor()
        override val colorDark = this@toPokemonTypeTheme.getDarkColor()
        override val name = this@toPokemonTypeTheme.name
    }
}

fun PokemonType.getLightColor(): Color {
    return when (this.type) {
        TYPE_GHOST -> {
            ghostLight
        }
        TYPE_STEEL -> {
            steelLight
        }
        TYPE_DRAGON -> {
            dragonLight
        }
        TYPE_FLYING -> {
            flyingLight
        }
        TYPE_WATER -> {
            waterLight
        }
        TYPE_ICE -> {
            iceLight
        }
        TYPE_GRASS -> {
            grassLight
        }
        TYPE_BUG -> {
            bugLight
        }
        TYPE_NORMAL -> {
            normalLight
        }
        TYPE_ELECTRIC -> {
            electricLight
        }
        TYPE_GROUND -> {
            groundLight
        }
        TYPE_ROCK -> {
            rockLight
        }
        TYPE_FIRE -> {
            fireLight
        }
        TYPE_FIGHTING -> {
            fightingLight
        }
        TYPE_DARK -> {
            darkLight
        }
        TYPE_PSYCHIC -> {
            psychicLight
        }
        TYPE_FAIRY -> {
            fairyLight
        }
        TYPE_POISON -> {
            poisonLight
        }
        else -> normalLight
    }
}

fun PokemonType.getDarkColor(): Color {
    return when (this.type) {
        TYPE_GHOST -> {
            ghostDark
        }
        TYPE_STEEL -> {
            steelDark
        }
        TYPE_DRAGON -> {
            dragonDark
        }
        TYPE_FLYING -> {
            flyingDark
        }
        TYPE_WATER -> {
            waterDark
        }
        TYPE_ICE -> {
            iceDark
        }
        TYPE_GRASS -> {
            grassDark
        }
        TYPE_BUG -> {
            bugDark
        }
        TYPE_NORMAL -> {
            normalDark
        }
        TYPE_ELECTRIC -> {
            electricDark
        }
        TYPE_GROUND -> {
            groundDark
        }
        TYPE_ROCK -> {
            rockDark
        }
        TYPE_FIRE -> {
            fireDark
        }
        TYPE_FIGHTING -> {
            fightingDark
        }
        TYPE_DARK -> {
            darkDark
        }
        TYPE_PSYCHIC -> {
            psychicDark
        }
        TYPE_FAIRY -> {
            fairyDark
        }
        TYPE_POISON -> {
            poisonDark
        }
        else -> normalDark
    }
}

fun Dp.toPx(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.value, context.resources.displayMetrics)
}
