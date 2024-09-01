package ir.runique.run.data.di

import ir.runique.run.data.CreateRunWorker
import ir.runique.run.data.FetchRunsWorker
import ir.runique.run.data.DeleteRunWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)
}