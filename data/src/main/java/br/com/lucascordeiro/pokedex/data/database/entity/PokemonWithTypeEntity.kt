package br.com.lucascordeiro.pokedex.data.database.entity

import androidx.room.*

data class PokemonWithTypeEntity(
    @Embedded val pokemonEntity: PokemonEntity,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "typeId",
       entity = PokemonTypeEntity::class,
        associateBy = Junction(
            value = PokemonCrossTypeEntity::class,
            parentColumn = "pokemonId",
            entityColumn = "typeId"
        )
    )
    val types: List<PokemonTypeEntity>
)