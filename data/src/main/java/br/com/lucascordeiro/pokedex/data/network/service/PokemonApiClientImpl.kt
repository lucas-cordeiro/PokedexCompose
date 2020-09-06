package br.com.lucascordeiro.pokedex.data.network.service

import br.com.lucascordeiro.pokedex.data.network.httpclient.AppHttpClient
import br.com.lucascordeiro.pokedex.data.network.model.PokemonNetwork
import br.com.lucascordeiro.pokedex.data.network.model.ResponsePokemonNetwork
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonApiClientImpl(private val appHttpClient: AppHttpClient) : PokemonApiClient{
    override suspend fun doGetPokemon() = appHttpClient.httpClient.get<ResponsePokemonNetwork<PokemonNetwork>>(basePath){
        parameter("offset", 0)
        parameter("limit", 10)
    }

    override suspend fun doGetPokemonById(id: Long) = appHttpClient.httpClient.get<PokemonNetwork>("$basePath/$id")

    override val basePath = "pokemon"
}