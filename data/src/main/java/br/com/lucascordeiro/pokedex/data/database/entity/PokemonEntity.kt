package br.com.lucascordeiro.pokedex.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey
    var pokemonId: Long = 0L,
    @ColumnInfo(name = "name")
    var name: String = "",
    var url: String = "",
    var imageUrl: String = "",
    @ColumnInfo(name = "isLike")
    var isLike: Boolean = false,
    @Ignore
    var types: List<PokemonTypeEntity>? = null
)