package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single { GetPokemonListUseCaseImpl(get(), get(), get()) as GetPokemonListUseCase }
    single { GetPokemonDetailUseCaseImpl(get(), get()) as GetPokemonDetailUseCase }
    single { SearchPokemonUseCaseImpl(get(),get(), get()) as SearchPokemonUseCase}
    single { UpdateLikePokemonUseCaseImpl(get(),get()) as UpdateLikePokemonUseCase}
    single { SaveSimplePokemonUseCaseImpl(get(),get()) as SaveSimplePokemonUseCase}
}
