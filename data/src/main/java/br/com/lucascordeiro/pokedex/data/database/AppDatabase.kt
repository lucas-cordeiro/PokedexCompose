package br.com.lucascordeiro.pokedex.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.lucascordeiro.pokedex.data.database.dao.PokemonDao
import br.com.lucascordeiro.pokedex.data.database.dao.PokemonTypeDao
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonCrossTypeEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonTypeEntity

@Database(entities = [PokemonEntity::class, PokemonTypeEntity::class, PokemonCrossTypeEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonTypeDao(): PokemonTypeDao
}
