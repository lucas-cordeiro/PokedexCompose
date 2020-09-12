package br.com.lucascordeiro.pokedex.compose.utils

import android.util.Log
import br.com.lucascordeiro.pokedex.compose.helper.PokemonTypeTheme
import br.com.lucascordeiro.pokedex.compose.ui.*
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.utils.*

fun PokemonType.toPokemonTypeTheme() : PokemonTypeTheme {
    var colorTypeLight = purple200
    var colorTypeDark = purple700

    when(this.type){
        TYPE_GHOST -> {
            colorTypeLight = ghostLight
            colorTypeDark = ghostDark
        }
        TYPE_STEEL -> {
            colorTypeLight = steelLight
            colorTypeDark = steelDark
        }
        TYPE_DRAGON -> {
            colorTypeLight = dragonLight
            colorTypeDark = dragonDark
        }
        TYPE_FLYING -> {
            colorTypeLight = flyingLight
            colorTypeDark = flyingDark
        }
        TYPE_WATER -> {
            colorTypeLight = waterLight
            colorTypeDark = waterDark
        }
        TYPE_ICE -> {
            colorTypeLight = iceLight
            colorTypeDark = iceDark
        }
        TYPE_GRASS -> {
            colorTypeLight = grassLight
            colorTypeDark = grassDark
        }
        TYPE_BUG -> {
            colorTypeLight = bugLight
            colorTypeDark = bugDark
        }
        TYPE_NORMAL -> {
            colorTypeLight = normalLight
            colorTypeDark = normalDark
        }
        TYPE_ELECTRIC -> {
            colorTypeLight = electricLight
            colorTypeDark = electricDark
        }
        TYPE_GROUND -> {
            colorTypeLight = groundLight
            colorTypeDark = groundDark
        }
        TYPE_ROCK -> {
            colorTypeLight = rockLight
            colorTypeDark = rockDark
        }
        TYPE_FIRE -> {
            colorTypeLight = fireLight
            colorTypeDark = fireDark
        }
        TYPE_FIGHTING -> {
            colorTypeLight = fightingLight
            colorTypeDark = fightingDark
        }
        TYPE_DARK -> {
            colorTypeLight = darkLight
            colorTypeDark = darkDark
        }
        TYPE_PSYCHIC -> {
            colorTypeLight = psychicLight
            colorTypeDark = psychicDark
        }
        TYPE_FAIRY -> {
            colorTypeLight = fairyLight
            colorTypeDark = fairyDark
        }
        TYPE_POISON -> {
            colorTypeLight = poisonLight
            colorTypeDark = poisonDark
        }
    }

    return object :PokemonTypeTheme{
        override var colorLight = colorTypeLight
        override val colorDark = colorTypeDark
        override val name = this@toPokemonTypeTheme.name
    }
}