package br.com.lucascordeiro.pokedex.compose.di.module

import br.com.lucascordeiro.pokedex.compose.BuildConfig
import br.com.lucascordeiro.pokedex.data.helper.ErrorHandlerImpl
import br.com.lucascordeiro.pokedex.data.network.httpclient.jsonserializer.provideSerializer
import br.com.lucascordeiro.pokedex.data.network.httpclient.logging.provideLoggingInterceptor
import br.com.lucascordeiro.pokedex.data.network.httpclient.provideHttpClient
import br.com.lucascordeiro.pokedex.data.network.httpclient.timeout.provideHttpTimeout
import br.com.lucascordeiro.pokedex.data.network.service.PokemonApiClient
import br.com.lucascordeiro.pokedex.data.network.service.PokemonApiClientImpl
import br.com.lucascordeiro.pokedex.domain.helper.ErrorHandler
import org.koin.dsl.module

val networkModule = module {
    factory { provideHttpTimeout() }
    factory { provideSerializer() }
    factory { provideLoggingInterceptor(BuildConfig.DEBUG) }
    factory { ErrorHandlerImpl() as ErrorHandler }
    single { provideHttpClient(BuildConfig.BASE_URL, get(), get(), get()) }
    single { PokemonApiClientImpl(get()) as PokemonApiClient }
}
