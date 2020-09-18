package br.com.lucascordeiro.pokedex.data.network.service

import br.com.lucascordeiro.pokedex.data.network.model.PokemonNetwork
import br.com.lucascordeiro.pokedex.data.network.model.ResponsePokemonNetwork

interface PokemonApiClient : ApiClient {
    suspend fun doGetPokemon(offset: Long, limit: Long): ResponsePokemonNetwork<PokemonNetwork>
    suspend fun doGetPokemonById(id: Long): PokemonNetwork
}