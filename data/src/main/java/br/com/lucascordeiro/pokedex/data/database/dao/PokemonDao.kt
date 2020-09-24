package br.com.lucascordeiro.pokedex.data.database.dao

import androidx.room.*
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonCrossTypeEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon ORDER BY pokemonId LIMIT :limit OFFSET :offset")
    fun getAll(limit: Long, offset: Long): Flow<List<PokemonEntity>>

    @Query("SELECT pokemonId FROM pokemon ORDER BY pokemonId LIMIT :limit OFFSET :offset")
    suspend fun getAllIds(limit: Long, offset: Long): List<Long>

    @Query("UPDATE pokemon SET isLike = :like WHERE pokemonId = :pokemonId")
    suspend fun updateLikeByPokemonId(pokemonId: Long, like: Boolean)

    @Query("SELECT * FROM pokemon WHERE pokemonId = :pokemonId")
    fun getById(pokemonId: Long): Flow<PokemonEntity?>

    @Query("SELECT * FROM pokemon WHERE name LIKE :nameQuery ORDER BY pokemonId LIMIT :limit")
    fun queryByName(nameQuery: String, limit: Long,): Flow<List<PokemonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(join: PokemonCrossTypeEntity)

    @Query("SELECT COUNT(*) FROM pokemon")
    suspend fun count(): Long

    @Transaction
    @Query("SELECT * FROM pokemoncrosstypeentity WHERE pokemonId = :pokemonId")
    suspend fun getPokemonWithTypeEntity(pokemonId: Long): List<PokemonCrossTypeEntity>
}
