package ir.runique.core.data.di

import ir.runique.core.data.auth.EncryptedDataStorage
import ir.runique.core.data.networking.HttpClientFactory
import ir.runique.core.data.run.OfflineFirstRunRepository
import ir.runique.core.domain.SessionStorage
import ir.runique.core.domain.run.RunRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedDataStorage).bind<SessionStorage>()
    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}