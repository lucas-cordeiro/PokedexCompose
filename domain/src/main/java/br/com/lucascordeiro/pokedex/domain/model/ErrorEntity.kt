package br.com.lucascordeiro.pokedex.domain.model

sealed class ErrorEntity {
    sealed class ApiError : ErrorEntity() {
        object Network : ErrorEntity()

        object NotFound : ErrorEntity()

        object AccessDenied : ErrorEntity()

        object Timeout: ErrorEntity()

        object Unknown : ErrorEntity()
    }
}