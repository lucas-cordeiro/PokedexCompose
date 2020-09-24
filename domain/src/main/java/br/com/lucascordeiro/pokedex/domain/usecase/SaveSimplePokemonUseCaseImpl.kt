package br.com.lucascordeiro.pokedex.domain.usecase

import br.com.lucascordeiro.pokedex.domain.model.PokemonSimple
import br.com.lucascordeiro.pokedex.domain.repository.PokemonRepository
import br.com.lucascordeiro.pokedex.domain.repository.PokemonSimpleRepository
import br.com.lucascordeiro.pokedex.domain.utils.TOTAL_POKEMON_COUNT
import kotlinx.coroutines.flow.first

class SaveSimplePokemonUseCaseImpl(
        private val pokemonRepository: PokemonRepository,
        private val pokemonSimpleRepository: PokemonSimpleRepository
) : SaveSimplePokemonUseCase {
    override suspend fun doSaveSimplePokemon() {
        if(pokemonSimpleRepository.doGetPokemonsIdsFromDatabase(0, TOTAL_POKEMON_COUNT).first().size < TOTAL_POKEMON_COUNT){
            pokemonSimpleRepository.doBulkInsertPokemonToDatabase(pokemonRepository.doGetPokemonsFromNetwork(0, TOTAL_POKEMON_COUNT).first().map { pokemon ->  PokemonSimple(
                    id = pokemon.id,
                    name = pokemon.name
            ) })
        }
    }
}