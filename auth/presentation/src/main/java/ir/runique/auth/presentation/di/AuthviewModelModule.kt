package ir.runique.auth.presentation.di

import ir.runique.auth.presentation.register.RegisterViewModel
import ir.runique.auth.presentation.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::LoginViewModel)
}