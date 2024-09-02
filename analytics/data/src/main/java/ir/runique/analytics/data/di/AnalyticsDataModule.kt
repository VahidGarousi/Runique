package ir.runique.analytics.data.di

import ir.runique.analytics.data.RoomAnalyticsRepository
import ir.runique.analytics.domain.AnalyticsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsDataModule = module {
    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()
}