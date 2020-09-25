package br.com.lucascordeiro.pokedex.data.repository

import android.util.Log
import androidx.core.net.toUri
import br.com.lucascordeiro.pokedex.data.database.dao.PokemonDao
import br.com.lucascordeiro.pokedex.data.database.dao.PokemonTypeDao
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonCrossTypeEntity
import br.com.lucascordeiro.pokedex.data.mapper.pokemon.PokemonMapper
import br.com.lucascordeiro.pokedex.data.network.service.PokemonApiClient
import br.com.lucascordeiro.pokedex.data.preferences.PreferenceController
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.*

class PokemonRepositoryImpl(
    private val pokemonApiClient: PokemonApiClient,
    private val pokemonDao: PokemonDao,
    private val pokemonTypeDao: PokemonTypeDao,
    private val pokemonMapper: PokemonMapper,
    private val preferenceController: PreferenceController
) : PokemonRepository {

    override suspend fun getPokemonByIdFromDatabase(pokemonId: Long) = pokemonDao.getById(pokemonId).map { pokemonEntity ->
       if(pokemonEntity!=null){
           val relation =
                   pokemonDao.getPokemonWithTypeEntity(pokemonId = pokemonEntity.pokemonId)
                           .map { pokemonCrossType ->
                               pokemonTypeDao.getById(pokemonCrossType.typeId).first()
                           }
           pokemonEntity.types = relation
           pokemonMapper.providePokemonEntityToPokemonMapper().map(pokemonEntity)
       }else null
    }

    override suspend fun getPokemonByIdFromNetwork(pokemonId: Long) = pokemonApiClient.getPokemonById(pokemonId).map {
        pokemonMapper.providePokemonNetworkMapper().map(it?.apply {
            id = pokemonId
        })
    }

    override suspend fun getPokemonsFromDatabase(offset: Long, limit: Long) = pokemonDao.getAll(offset = offset, limit = limit).map { pokemons->
        pokemons.map { pokemonEntity ->
            val relation =
                pokemonDao.getPokemonWithTypeEntity(pokemonId = pokemonEntity.pokemonId)
                    .map { pokemonCrossType ->
                        pokemonTypeDao.getById(pokemonCrossType.typeId).first()
                    }
            pokemonEntity.types = relation
            pokemonMapper.providePokemonEntityToPokemonMapper().map(pokemonEntity)!!
        }
    }

    override suspend fun getPokemonsFromNetwork(offset: Long, limit: Long) = pokemonApiClient.getPokemon(offset = offset, limit = limit).map { result->
        result.results?.map {pokemon ->
            pokemonMapper.providePokemonNetworkMapper().map(pokemon.apply {
                id = url?.toUri()?.lastPathSegment?.toLong()
            })!!
        }?: emptyList()
    }

    override suspend fun getPokemonsIdsFromDatabase(offset: Long, limit: Long) = pokemonDao.getAllIds(offset = offset, limit = limit)

    override suspend fun insertPokemonToDatabase(pokemon: Pokemon) {
        pokemonDao.insertAll(listOf(pokemonMapper.providePokemonToPokemonEntityMapper().map(pokemon)))
        pokemon.type.forEach { pokemonType ->
            val type =
                pokemonMapper.providePokemonTypeToPokemonTypeEntityMapper().map(pokemonType)
            var pokemonTypeId = pokemonTypeDao.insert(type)
            if (pokemonTypeId == -1L) {
                pokemonTypeId =
                    pokemonTypeDao.getByName(type.name ?: "").first().typeId ?: 0L
            }
            pokemonDao.insert(
                PokemonCrossTypeEntity(
                    pokemonId = pokemon.id,
                    typeId = pokemonTypeId
                )
            )
        }
    }

    override suspend fun searchPokemonByNameFromDatabase(nameQuery: String, limit: Long) = pokemonDao.queryByName("${nameQuery}%", limit).map { pokemons->
        Log.d("BUG","pokemons: $pokemons")
        pokemons.map { pokemonEntity ->
            val relation =
                    pokemonDao.getPokemonWithTypeEntity(pokemonId = pokemonEntity.pokemonId)
                            .map { pokemonCrossType ->
                                pokemonTypeDao.getById(pokemonCrossType.typeId).first()
                            }
            pokemonEntity.types = relation
            pokemonMapper.providePokemonEntityToPokemonMapper().map(pokemonEntity)!!
        }
    }

    override suspend fun updateLikePokemonById(pokemonId: Long, like: Boolean) = pokemonDao.updateLikeByPokemonId(
            pokemonId = pokemonId,
            like = like
    )

    override suspend fun bulkInsertPokemonToDatabase(pokemons: List<Pokemon>) {
        pokemons.forEach { pokemon ->
            insertPokemonToDatabase(pokemon)
        }
    }
}