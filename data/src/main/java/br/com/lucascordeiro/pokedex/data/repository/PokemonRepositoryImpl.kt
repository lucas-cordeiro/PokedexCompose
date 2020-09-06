package br.com.lucascordeiro.pokedex.data.repository

import android.util.Log
import androidx.core.net.toUri
import br.com.lucascordeiro.pokedex.data.mapper.Mapper
import br.com.lucascordeiro.pokedex.data.network.model.PokemonNetwork
import br.com.lucascordeiro.pokedex.data.network.service.PokemonApiClient
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PokemonRepositoryImpl(
    private val pokemonApiClient: PokemonApiClient,
    private val pokemonDataMapper: Mapper<PokemonNetwork, Pokemon>
) : PokemonRepository {
    override suspend fun doGetCurrentTime(): Long {
        return System.currentTimeMillis()
    }

    override suspend fun doGetLastCacheUpdate(): Long {
        return 0L
    }

    override suspend fun doUpdateLastCacheUpdate(time: Long) {

    }

    override fun doGetPokemonFromDatabase(): Flow<List<Pokemon>> {
        return flowOf()
    }

    override suspend fun doGetPokemonFromNetwork() : List<Pokemon> {
        val response = pokemonApiClient.doGetPokemon().results.orEmpty()
        return response.map { pokemonNetwork ->
            pokemonNetwork.apply {
                id = url?.toUri()?.lastPathSegment?.toLong()
            }
            val pokemon = pokemonDataMapper.map(pokemonNetwork)

            val pokemonById = pokemonApiClient.doGetPokemonById(pokemon.id)
            pokemon.imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
            pokemon.type = pokemonById.types?.filter { it.type!=null }?.map { pokemonTypeNetwork ->
                PokemonType.valueOf(pokemonTypeNetwork.type?.name?.toUpperCase()?:"NORMAL")
            }.orEmpty()

            pokemon
        }
    }

    override suspend fun doInsertPokemonDatabase(list: List<Pokemon>) {

    }
}