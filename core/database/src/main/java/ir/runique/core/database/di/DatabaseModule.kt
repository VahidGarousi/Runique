package ir.runique.core.database.di

import androidx.room.Room
import ir.runique.core.database.RunDatabase
import ir.runique.core.domain.run.LocalRunDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ir.runique.core.database.RoomLocalRunDataSource
import org.koin.dsl.bind

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            RunDatabase::class.java,
            "run.db"
        ).build()
    }
    single { get<RunDatabase>().runDao }

    singleOf(::RoomLocalRunDataSource).bind<LocalRunDataSource>()
}