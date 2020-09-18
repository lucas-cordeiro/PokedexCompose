package br.com.lucascordeiro.pokedex.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pokemonType",
    indices = arrayOf(Index(value = ["name"], unique = true))
)
data class PokemonTypeEntity (
    @PrimaryKey(autoGenerate = true)
    var typeId: Long? = null,
    var name: String? = null
)