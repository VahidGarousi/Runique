package ir.runique

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import ir.runique.core.designsystem.RuniqueTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private lateinit var splitInstallManager: SplitInstallManager
    private val splitInstallStateUpdatedListener = SplitInstallStateUpdatedListener { state ->
        when (state.status()) {
            SplitInstallSessionStatus.INSTALLED -> {
                viewModel.setAnalyticsInstallingDialogVisibility(isVisible = false)
                viewModel.setAnalyticsInstallingDialogVisibility(isVisible = false)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.analytics_installed),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            SplitInstallSessionStatus.DOWNLOADING -> {
                viewModel.setAnalyticsInstallingDialogVisibility(isVisible = true)
            }

            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                splitInstallManager.startConfirmationDialogForResult(state, this, 0)
            }

            SplitInstallSessionStatus.INSTALLING -> {
                viewModel.setAnalyticsInstallingDialogVisibility(isVisible = true)
            }

            SplitInstallSessionStatus.FAILED -> {

                viewModel.setAnalyticsInstallingDialogVisibility(isVisible = false)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.installation_failed),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
        }
        splitInstallManager = SplitInstallManagerFactory.create(applicationContext)
        setContent {
            RuniqueTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (!viewModel.state.isCheckingAuth) {
                        val navController = rememberNavController()
                        NavigationRoot(
                            navController = navController,
                            isLoggedIn = viewModel.state.isLoggedIn,
                            onAnalyticsClick = {
                                installOrStartAnalyticFeature()
                            }
                        )
                        if (viewModel.state.showAnalyticsInstallingDialog) {
                            Dialog(
                                onDismissRequest = {}
                            ) {
                                Column(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                    Spacer(Modifier.height(8.dp))
                                    Text(stringResource(R.string.installing_analytics_module))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        splitInstallManager.registerListener(splitInstallStateUpdatedListener)
    }

    override fun onPause() {
        super.onPause()
        splitInstallManager.unregisterListener(splitInstallStateUpdatedListener)
    }

    private fun installOrStartAnalyticFeature() {
        if (splitInstallManager.installedModules.contains("analytics_feature")) {
            Intent()
                .setClassName(
                    packageName,
                    "ir.runique.analytics.analytics_feature.AnalyticsActivity"
                )
                .also(::startActivity)
            return
        }
        val request = SplitInstallRequest.newBuilder()
            .addModule("analytics_feature")
            .build()
        splitInstallManager.startInstall(request)
            .addOnFailureListener {
                it.printStackTrace()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.could_not_load_module),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }
}
