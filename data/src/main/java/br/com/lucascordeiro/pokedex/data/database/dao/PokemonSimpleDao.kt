package br.com.lucascordeiro.pokedex.data.database.dao

import androidx.room.*
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonSimpleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonSimpleDao {

    @Query("SELECT pokemonId FROM pokemonSimple ORDER BY pokemonId LIMIT :limit OFFSET :offset")
    suspend fun getAllIds(limit: Long, offset: Long): List<Long>

    @Query("SELECT * FROM pokemonSimple ORDER BY pokemonId LIMIT :limit OFFSET :offset")
    fun getAll(limit: Long, offset: Long): Flow<List<PokemonSimpleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<PokemonSimpleEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(join: PokemonSimpleEntity)

    @Query("SELECT COUNT(*) FROM pokemonSimple")
    suspend fun count(): Long
}
