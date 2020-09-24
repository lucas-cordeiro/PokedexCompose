package br.com.lucascordeiro.pokedex.data.database.entity

import androidx.room.*

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