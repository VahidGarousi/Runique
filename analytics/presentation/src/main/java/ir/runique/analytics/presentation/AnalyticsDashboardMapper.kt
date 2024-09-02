package ir.runique.analytics.presentation

import ir.runique.analytics.domain.AnalyticsValues
import ir.runique.core.ui.formatted
import ir.runique.core.ui.toFormattedKm
import ir.runique.core.ui.toFormattedKmh
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

fun Duration.toFormattedTotalTime(): String {
    val days = toLong(DurationUnit.DAYS)
    val hours = toLong(DurationUnit.HOURS) % 24
    val minutes = toLong(DurationUnit.MINUTES) % 60
    return "${days}d : ${hours}h : ${minutes}m"
}

fun AnalyticsValues.mapToAnalyticsDashboardState() = AnalyticsDashboardState(
    totalDistanceRun = (totalDistanceRun / 1000.0).toFormattedKm(),
    totalTimeRun = totalTimeRun.toFormattedTotalTime(),
    fastestEverRun = fastestEverRun.toFormattedKmh(),
    avgDistancePerRun = (avgDistancePerRun / 1000.0).toFormattedKm(),
    avgPacePerRun = avgPacePerRun.seconds.formatted()
)