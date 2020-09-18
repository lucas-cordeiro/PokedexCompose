package br.com.lucascordeiro.pokedex.data.database.dao

import androidx.room.*
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonTypeDao {
    @Query("SELECT * FROM pokemonType")
    fun getAll(): Flow<List<PokemonTypeEntity>>

    @Query("SELECT * FROM pokemonType where typeId = :pokemonTypeId")
    fun getById(pokemonTypeId: Long): Flow<PokemonTypeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(pokemonTypeEntity: PokemonTypeEntity): Long

    @Query("SELECT * FROM pokemonType where name = :name")
    fun getByName(name: String): Flow<PokemonTypeEntity>
}
