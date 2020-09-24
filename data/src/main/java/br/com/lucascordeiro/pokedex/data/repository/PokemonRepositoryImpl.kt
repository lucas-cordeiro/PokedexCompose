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
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.utils.TOTAL_POKEMON_COUNT
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*

class PokemonRepositoryImpl(
    private val pokemonApiClient: PokemonApiClient,
    private val pokemonDao: PokemonDao,
    private val pokemonTypeDao: PokemonTypeDao,
    private val pokemonMapper: PokemonMapper,
    private val preferenceController: PreferenceController
) : PokemonRepository {

    override suspend fun doGetPokemonByIdFromDatabase(pokemonId: Long) = pokemonDao.getById(pokemonId).map { pokemonEntity ->
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

    override suspend fun doGetPokemonByIdFromNetwork(pokemonId: Long) = pokemonApiClient.doGetPokemonById(pokemonId).map {
        pokemonMapper.providePokemonNetworkMapper().map(it?.apply {
            id = pokemonId
        })
    }

    override suspend fun doGetPokemonsFromDatabase(offset: Long, limit: Long) = pokemonDao.getAll(offset = offset, limit = limit).map { pokemons->
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

    override suspend fun doGetPokemonsFromNetwork(offset: Long, limit: Long) = pokemonApiClient.doGetPokemon(offset = offset, limit = limit).map {result->
        result.results?.map {pokemon ->
            pokemonMapper.providePokemonNetworkMapper().map(pokemon.apply {
                id = url?.toUri()?.lastPathSegment?.toLong()
            })!!
        }?: emptyList()
    }

    override suspend fun doGetPokemonsIdsFromDatabase(offset: Long, limit: Long) = flowOf(pokemonDao.getAllIds(offset = offset, limit = limit))

    override suspend fun doInsertPokemonToDatabase(pokemon: Pokemon) {
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

    override suspend fun doSearchByPokemonNameFromDatabase(nameQuery: String, limit: Long) = pokemonDao.queryByName("${nameQuery}%", limit).map { pokemons->
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

    override suspend fun doUpdateLikePokemonById(pokemonId: Long, like: Boolean) = pokemonDao.updateLikeByPokemonId(
            pokemonId = pokemonId,
            like = like
    )

    override suspend fun doBulkInsertPokemonToDatabase(pokemons: List<Pokemon>) {
        pokemons.forEach { pokemon ->
            doInsertPokemonToDatabase(pokemon)
        }
    }
}