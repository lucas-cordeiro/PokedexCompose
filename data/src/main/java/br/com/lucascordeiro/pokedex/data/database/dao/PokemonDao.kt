package br.com.lucascordeiro.pokedex.data.database.dao

import androidx.room.*
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonCrossTypeEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonWithTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon ORDER BY pokemonId LIMIT :limit OFFSET :offset")
    fun getAll(limit: Long, offset: Long) : Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE pokemonId = :pokemonId")
    fun getById(pokemonId: Long) : Flow<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<PokemonEntity>)



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(join: PokemonCrossTypeEntity)

    @Transaction
    @Query("SELECT * FROM pokemoncrosstypeentity WHERE pokemonId = :pokemonId")
    suspend fun getPokemonWithTypeEntity(pokemonId: Long): List<PokemonCrossTypeEntity>

}