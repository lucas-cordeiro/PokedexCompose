package br.com.lucascordeiro.pokedex.data.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}