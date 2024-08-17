package ir.runique.auth.data.di

import ir.runique.auth.data.EmailPatternValidator
import ir.runique.auth.data.repository.AuthRepositoryImpl
import ir.runique.auth.domain.AuthRepository
import ir.runique.auth.domain.PatternValidator
import ir.runique.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}