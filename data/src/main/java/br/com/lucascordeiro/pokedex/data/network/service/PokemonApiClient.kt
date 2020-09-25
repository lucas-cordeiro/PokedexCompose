package br.com.lucascordeiro.pokedex.data.network.service

import br.com.lucascordeiro.pokedex.data.network.model.PokemonNetwork
import br.com.lucascordeiro.pokedex.data.network.model.ResponsePokemonNetwork
import kotlinx.coroutines.flow.Flow

interface PokemonApiClient : ApiClient {
    suspend fun getPokemon(offset: Long, limit: Long): Flow<ResponsePokemonNetwork<PokemonNetwork>>
    suspend fun getPokemonById(id: Long): Flow<PokemonNetwork?>
}