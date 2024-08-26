package ir.runique

import android.app.Application
import ir.runique.auth.data.di.authDataModule
import ir.runique.auth.presentation.di.authViewModelModule
import ir.runique.core.data.di.coreDataModule
import ir.runique.core.database.di.databaseModule
import ir.runique.di.appModule
import ir.runique.run.location.di.locationModule
import ir.runique.run.presentation.di.runPresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
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
                runPresentationModule,
                locationModule,
                databaseModule
            )
        }
    }
}