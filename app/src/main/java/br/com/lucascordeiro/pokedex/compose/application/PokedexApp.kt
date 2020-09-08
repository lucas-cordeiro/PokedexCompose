package br.com.lucascordeiro.pokedex.compose.application

import android.app.Application
import br.com.lucascordeiro.pokedex.compose.di.module.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PokedexApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PokedexApp)
            modules(provideModules())
        }
    }

    fun provideModules() = listOf(
            networkModule,
            repositoryModule,
            useCaseModule,
            mapperModule,
            databaseModule,
            sharedPreferenceModule
    )
}