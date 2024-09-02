package ir.runique.analytics.analytics_feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.splitcompat.SplitCompat
import ir.runique.analytics.data.di.analyticsDataModule
import ir.runique.analytics.presentation.AnalyticsDashboardScreenScreenRoot
import ir.runique.core.designsystem.RuniqueTheme
import org.koin.core.context.loadKoinModules

class AnalyticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(analyticsDataModule)
        SplitCompat.installActivity(this)
        setContent {
            RuniqueTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController, startDestination = "analytics_dashboard"
                ) {
                    composable("analytics_dashboard") {
                        AnalyticsDashboardScreenScreenRoot(
                            onBackClick = { finish() }
                        )
                    }
                }
            }
        }
    }
}