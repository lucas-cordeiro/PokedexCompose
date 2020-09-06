package br.com.lucascordeiro.pokedex.domain.model

sealed class ErrorEntity {
    sealed class ApiError : ErrorEntity() {
        object Network : ErrorEntity()

        object NotFound : ErrorEntity()

        object Timeout: ErrorEntity()

        object Unknown : ErrorEntity()

        object Unauthorized: ErrorEntity()
    }
    sealed class App : ErrorEntity() {
        object Unknown : ErrorEntity()
    }
}