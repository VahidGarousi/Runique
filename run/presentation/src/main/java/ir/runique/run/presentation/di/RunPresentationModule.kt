package ir.runique.run.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ir.runique.run.presentation.run_overview.RunOverviewViewModel
val runViewModelModule = module {
    viewModelOf(::RunOverviewViewModel)
}