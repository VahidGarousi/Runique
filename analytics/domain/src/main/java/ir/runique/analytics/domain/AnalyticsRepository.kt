package ir.runique.analytics.domain

interface AnalyticsRepository {
    suspend fun getAnalyticsValues() : AnalyticsValues
}