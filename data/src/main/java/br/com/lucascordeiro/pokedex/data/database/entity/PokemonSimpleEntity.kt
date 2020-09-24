package br.com.lucascordeiro.pokedex.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemonSimple")
data class PokemonSimpleEntity(
        @PrimaryKey
        var pokemonId: Long = 0L,
        @ColumnInfo(name = "name")
        var name: String = ""
)