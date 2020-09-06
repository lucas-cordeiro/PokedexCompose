package br.com.lucascordeiro.pokedex.data.mapper

import br.com.lucascordeiro.pokedex.data.network.model.PokemonNetwork
import br.com.lucascordeiro.pokedex.domain.model.Pokemon
import br.com.lucascordeiro.pokedex.domain.model.PokemonType

fun providePokemonNetworkMapper() = object : Mapper<PokemonNetwork, Pokemon> {
    override fun map(input: PokemonNetwork): Pokemon {
        return Pokemon(
            id = input.id?:0L,
            name = input.name?:"",
            type = listOf(PokemonType.NORMAL),
            imageUrl = ""
        )
    }
}