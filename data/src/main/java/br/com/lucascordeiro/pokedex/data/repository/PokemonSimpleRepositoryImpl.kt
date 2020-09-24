package br.com.lucascordeiro.pokedex.data.repository

import br.com.lucascordeiro.pokedex.data.database.dao.PokemonSimpleDao
import br.com.lucascordeiro.pokedex.data.mapper.pokemonsimple.PokemonSimpleMapper
import br.com.lucascordeiro.pokedex.domain.model.PokemonSimple
import br.com.lucascordeiro.pokedex.domain.repository.PokemonSimpleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class PokemonSimpleRepositoryImpl(
        private val pokemonSimpleMapper: PokemonSimpleMapper,
        private val pokemonSimpleDao: PokemonSimpleDao
) : PokemonSimpleRepository {
    override suspend fun doBulkInsertPokemonToDatabase(pokemons: List<PokemonSimple>) = pokemonSimpleDao.insertAll(pokemons.map { pokemonSimpleMapper.providePokemonToPokemonEntityMapper().map(it) })

    override suspend fun doGetPokemonsFromDatabase(offset: Long, limit: Long) = pokemonSimpleDao.getAll(offset = offset, limit = limit)
            .map { pokemons ->
                pokemons.map { pokemon ->
                    pokemonSimpleMapper.providePokemonEntityToPokemonMapper().map(pokemon)!!
                }
            }

    override suspend fun doGetPokemonsIdsFromDatabase(offset: Long, limit: Long) =  flowOf(pokemonSimpleDao.getAllIds(offset = offset, limit = limit))

    override suspend fun doInsertPokemonToDatabase(pokemon: PokemonSimple) {
        pokemonSimpleDao.insert(pokemonSimpleMapper.providePokemonToPokemonEntityMapper().map(pokemon))
    }
}