package ir.runique.analytics.presentation

sealed interface AnalyticsAction {
    data object OnBackClick : AnalyticsAction
}