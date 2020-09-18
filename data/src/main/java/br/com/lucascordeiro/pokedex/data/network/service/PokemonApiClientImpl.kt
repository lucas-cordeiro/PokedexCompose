package br.com.lucascordeiro.pokedex.data.network.service

import br.com.lucascordeiro.pokedex.data.network.httpclient.AppHttpClient
import br.com.lucascordeiro.pokedex.data.network.model.PokemonNetwork
import br.com.lucascordeiro.pokedex.data.network.model.ResponsePokemonNetwork
import io.ktor.client.request.*

class PokemonApiClientImpl(private val appHttpClient: AppHttpClient) : PokemonApiClient {
    override suspend fun doGetPokemon(
        offset: Long,
        limit: Long
    ) = appHttpClient.httpClient.get<ResponsePokemonNetwork<PokemonNetwork>>(basePath) {
        parameter("offset", offset)
        parameter("limit", limit)
    }

    override suspend fun doGetPokemonById(id: Long) = appHttpClient.httpClient.get<PokemonNetwork>("$basePath/$id")

    override val basePath = "pokemon"
}