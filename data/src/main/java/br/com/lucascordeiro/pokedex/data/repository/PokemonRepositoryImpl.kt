package br.com.lucascordeiro.pokedex.data.repository

import android.util.Log
import androidx.core.net.toUri
import br.com.lucascordeiro.pokedex.data.database.dao.PokemonDao
import br.com.lucascordeiro.pokedex.data.database.dao.PokemonTypeDao
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonCrossTypeEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonEntity
import br.com.lucascordeiro.pokedex.data.database.entity.PokemonTypeEntity
import br.com.lucascordeiro.pokedex.data.mapper.Mapper
import br.com.lucascordeiro.pokedex.data.mapper.pokemon.PokemonMapper
import br.com.lucascordeiro.pokedex.data.mapper.pokemon.PokemonMapperImpl
import br.com.lucascordeiro.pokedex.data.network.service.PokemonApiClient
import br.com.lucascordeiro.pokedex.data.preferences.PreferenceController
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PokemonRepositoryImpl(
    private val pokemonApiClient: PokemonApiClient,
    private val pokemonDao: PokemonDao,
    private val pokemonTypeDao: PokemonTypeDao,
    private val pokemonMapper: PokemonMapper,
    private val preferenceController: PreferenceController
) : PokemonRepository {
    override suspend fun doGetCurrentTime(): Long {
        return System.currentTimeMillis()
    }

    override suspend fun doGetLastCacheUpdate(): Long {
        return preferenceController.lastCacheTime
    }

    override suspend fun doUpdateLastCacheUpdate(time: Long) {
        preferenceController.lastCacheTime = time
    }

    override fun doGetPokemonFromDatabase() = pokemonDao.getAll().map { it.map { pokemonEntity ->
        val relation = pokemonDao.getPokemonWithTypeEntity(pokemonId = pokemonEntity.pokemonId).map { pokemonCrossType ->
            pokemonTypeDao.getById(pokemonCrossType.typeId).first()
        }
        pokemonEntity.types = relation
        pokemonMapper.providePokemonEntityToPokemonMapper().map(pokemonEntity)
    } }

    override fun doGetPokemonByIdFromDatabase(pokemonId: Long) = pokemonDao.getById(pokemonId)
        .map { pokemonEntity ->
            val relation = pokemonDao.getPokemonWithTypeEntity(pokemonId = pokemonEntity.pokemonId).map { pokemonCrossType ->
                pokemonTypeDao.getById(pokemonCrossType.typeId).first()
            }
            pokemonEntity.types = relation
            pokemonMapper.providePokemonEntityToPokemonMapper().map(pokemonEntity)
        }

    override suspend fun doGetPokemonFromNetwork() : List<Pokemon> {
        val response = pokemonApiClient.doGetPokemon().results.orEmpty()
        return response.map { pokemonNetwork ->
            pokemonNetwork.apply {
                id = url?.toUri()?.lastPathSegment?.toLong()
            }
            val pokemon = pokemonMapper.providePokemonNetworkMapper().map(pokemonNetwork)

            val pokemonById = pokemonApiClient.doGetPokemonById(pokemon.id)
            pokemon.imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
            pokemon.type = pokemonById.types?.filter { it.type!=null }?.map { pokemonTypeNetwork ->
                PokemonType.valueOf(pokemonTypeNetwork.type?.name?.toUpperCase()?:"NORMAL")
            }.orEmpty()

            pokemon
        }
    }

    override suspend fun doInsertPokemonDatabase(list: List<Pokemon>) {
        pokemonDao.insertAll(list.map {
            it.type.forEach {pokemonType ->
                val type = pokemonMapper.providePokemonTypeToPokemonTypeEntityMapper().map(pokemonType)
                var pokemonTypeId = pokemonTypeDao.insert(type)
                if(pokemonTypeId==-1L){
                    pokemonTypeId = pokemonTypeDao.getByName(type.name?:"").first().typeId?:0L
                }
                pokemonDao.insert(PokemonCrossTypeEntity(pokemonId = it.id, typeId = pokemonTypeId))
            }
            pokemonMapper.providePokemonToPokemonEntityMapper().map(it)
        })
    }
}