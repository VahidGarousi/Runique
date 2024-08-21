package ir.runique.run.presentation.di

import ir.runique.run.domain.RunningTracker
import ir.runique.run.presentation.active_run.ActiveRunViewModel
import ir.runique.run.presentation.run_overview.RunOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val runPresentationModule = module {
    viewModelOf(::RunOverviewViewModel)
    viewModelOf(::ActiveRunViewModel)
    singleOf(::RunningTracker)
}