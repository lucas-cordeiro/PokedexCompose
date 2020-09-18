package br.com.lucascordeiro.pokedex.compose.di.module

import androidx.room.Room
import br.com.lucascordeiro.pokedex.data.database.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "br.com.lucas.cordeiro.pokedex.compose.database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().pokemonDao() }
    single { get<AppDatabase>().pokemonTypeDao() }
}
