package br.com.lucascordeiro.pokedex.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["pokemonId", "typeId"])
data class PokemonCrossTypeEntity(
    val pokemonId: Long,
    val typeId: Long
)