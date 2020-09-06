package br.com.lucascordeiro.pokedex.helper

import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity
import java.net.http.HttpTimeoutException

class FakeErrorHandler : ErrorHandler {
    override fun getError(throwable: Throwable): ErrorEntity {
        return when(throwable){
            is HttpTimeoutException -> ErrorEntity.ApiError.Timeout
            else -> ErrorEntity.ApiError.Unknown
        }
    }
}