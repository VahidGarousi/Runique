package ir.runique

import android.app.Application
import ir.runique.auth.data.di.authDataModule
import ir.runique.auth.presentation.di.authViewModelModule
import ir.runique.core.data.di.coreDataModule
import ir.runique.di.appModule
import ir.runique.run.presentation.di.runViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidLogger()
            androidContext(this@RuniqueApplication)
            modules(
                authDataModule,
                authViewModelModule,
                coreDataModule,
                appModule,
                runViewModelModule
            )
        }
    }
}