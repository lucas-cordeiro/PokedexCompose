package br.com.lucascordeiro.pokedex.domain.helper

import br.com.lucascordeiro.pokedex.domain.model.ErrorEntity

interface ErrorHandler {
    fun getError(throwable: Throwable): ErrorEntity
}