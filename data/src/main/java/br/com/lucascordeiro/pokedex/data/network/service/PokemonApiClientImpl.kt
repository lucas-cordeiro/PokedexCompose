package br.com.lucascordeiro.pokedex.data.network.service

import br.com.lucascordeiro.pokedex.data.network.httpclient.AppHttpClient
import br.com.lucascordeiro.pokedex.data.network.model.PokemonNetwork
import br.com.lucascordeiro.pokedex.data.network.model.ResponsePokemonNetwork
import io.ktor.client.request.*
import kotlinx.coroutines.flow.flowOf

class PokemonApiClientImpl(private val appHttpClient: AppHttpClient) : PokemonApiClient {
    override suspend fun getPokemon(
        offset: Long,
        limit: Long
    ) = flowOf(appHttpClient.httpClient.get<ResponsePokemonNetwork<PokemonNetwork>>(basePath) {
        parameter("offset", offset)
        parameter("limit", limit)
    })

    override suspend fun getPokemonById(id: Long) = flowOf(appHttpClient.httpClient.get<PokemonNetwork>("$basePath/$id"))

    override val basePath = "pokemon"
}