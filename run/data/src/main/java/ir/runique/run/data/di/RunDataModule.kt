package ir.runique.run.data.di

import ir.runique.core.domain.run.SyncRunScheduler
import ir.runique.run.data.CreateRunWorker
import ir.runique.run.data.DeleteRunWorker
import ir.runique.run.data.FetchRunsWorker
import ir.runique.run.data.SyncWorkerRunScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)
    singleOf(::SyncWorkerRunScheduler).bind<SyncRunScheduler>()
}