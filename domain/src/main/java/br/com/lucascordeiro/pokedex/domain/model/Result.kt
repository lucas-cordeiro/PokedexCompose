package br.com.lucascordeiro.pokedex.domain.model

sealed class Result<T> {

    data class Success<T>(val data: T) : Result<T>()

    data class Error<T>(val error: ErrorEntity) : Result<T>()
}