package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single { PokemonListUseCaseImpl(get(), get(), get()) as PokemonsListUseCase }
    single { PokemonDetailUseCaseImpl(get(), get()) as PokemonDetailUseCase }
    single { PokemonSearchUseCaseImpl(get(),get(), get()) as PokemonSearchUseCase}
    single { PokemonLikeUseCaseImpl(get(),get()) as PokemonLikeUseCase}
    single { SaveSimplePokemonUseCaseImpl(get(),get()) as SaveSimplePokemonUseCase}
}
