package br.com.lucascordeiro.pokedex.data.helper

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import io.ktor.client.features.*
import io.ktor.http.*
import java.nio.channels.UnresolvedAddressException

class ErrorHandlerImpl : ErrorHandler {
    override fun getError(throwable: Throwable): ErrorEntity {
        return when(throwable){
            is ClientRequestException -> {
                when(throwable.response.status.value){
                    401 -> ErrorEntity.ApiError.Unauthorized
                    404 -> ErrorEntity.ApiError.NotFound
                    408 -> ErrorEntity.ApiError.Timeout
                    else -> ErrorEntity.ApiError.Unknown
                }
            }
            is UnresolvedAddressException -> ErrorEntity.ApiError.Network
            else -> ErrorEntity.App.Unknown
        }
    }
}