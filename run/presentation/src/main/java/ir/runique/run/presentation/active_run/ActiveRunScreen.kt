@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package ir.runique.run.presentation.active_run

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.runique.core.designsystem.RuniqueTheme
import ir.runique.core.designsystem.StartIcon
import ir.runique.core.designsystem.StopIcon
import ir.runique.core.designsystem.components.RuniqueDialog
import ir.runique.core.designsystem.components.RuniqueFloatingActionButton
import ir.runique.core.designsystem.components.RuniqueOutlinedActionButton
import ir.runique.core.designsystem.components.RuniqueScaffold
import ir.runique.core.designsystem.components.RuniqueToolbar
import ir.runique.run.presentation.R
import ir.runique.run.presentation.active_run.components.RunDataCard
import ir.runique.run.presentation.util.hasLocationPermission
import ir.runique.run.presentation.util.hasNotificationPermission
import ir.runique.run.presentation.util.shouldShowLocationPermissionRationale
import ir.runique.run.presentation.util.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel()
) {
    ActiveRunScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                ActiveRunAction.OnBackClick -> TODO()
                else -> Unit
            }
            viewModel.onAction(action = action)
        }
    )
}


@Composable
fun ActiveRunScreen(
    state: ActiveRunState,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasFineLocationPermission =
            perms[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasCoarseLocationPermission =
            perms[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            perms[android.Manifest.permission.POST_NOTIFICATIONS] == true
        } else true

        val activity = (context as ComponentActivity)
        val shouldShowLocationPermissionRationale = activity.shouldShowLocationPermissionRationale()
        val shouldShowNotificationPermissionRationale =
            activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = hasCoarseLocationPermission && hasFineLocationPermission,
                showLocationRationale = shouldShowLocationPermissionRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = shouldShowNotificationPermissionRationale,
                showNotificationRationale = hasNotificationPermission
            )
        )
    }
    LaunchedEffect(
        true
    ) {
        val activity = (context as ComponentActivity)
        val shouldShowLocationRationale = activity.shouldShowLocationPermissionRationale()
        val shouldShowNotificationRationale =
            activity.shouldShowNotificationPermissionRationale()
        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationRationale = shouldShowLocationRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = shouldShowNotificationRationale,
                showNotificationRationale = context.hasNotificationPermission()
            )
        )
        if (!shouldShowNotificationRationale && !shouldShowLocationRationale) {
            permissionLauncher.requestRuniquePermissions(context = context)
        }
    }
    RuniqueScaffold(
        withGradient = false,
        topAppBar = {
            RuniqueToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClick = {
                    onAction(ActiveRunAction.OnBackClick)
                }
            )
        },
        floatingActionButton = {
            RuniqueFloatingActionButton(
                icon = if (state.shouldTrack) StopIcon else StartIcon,
                onClick = { onAction(ActiveRunAction.OnToggleRunClick) },
                iconSize = 20.dp,
                contentDescription = if (state.shouldTrack) {
                    stringResource(id = R.string.pause_run)
                } else {
                    stringResource(id = R.string.start_run)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            RunDataCard(
                runData = state.runData,
                elapsedTime = state.elapsedTime,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(padding)
                    .fillMaxWidth()
            )
        }
    }
    if (state.showLocationRationale || state.showNotificationRationale) {
        RuniqueDialog(
            title = stringResource(id = R.string.permission_required),
            description = when {
                state.showLocationRationale && state.showNotificationRationale -> {
                    stringResource(id = R.string.location_notification_rationale)
                }

                state.showLocationRationale -> {
                    stringResource(id = R.string.location_rationale)
                }

                else -> {
                    stringResource(id = R.string.notification_rationale)
                }
            },
            primaryButton = {
                RuniqueOutlinedActionButton(
                    text = stringResource(id = R.string.okay),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.OnDismissRationaleDialog)
                        permissionLauncher.requestRuniquePermissions(context = context)
                    }
                )
            },
            onDismiss = { /**/ },
            secondaryButton = {

            }
        )
    }
}


private fun ActivityResultLauncher<Array<String>>.requestRuniquePermissions(
    context: Context
) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()
    val locationPermission = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )
    val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else arrayOf()
    when {
        !hasLocationPermission && !hasNotificationPermission -> {
            launch(locationPermission + notificationPermission)
        }

        !hasLocationPermission -> {
            launch(locationPermission)
        }

        !hasNotificationPermission -> {
            launch(notificationPermission)
        }
    }
}

@Preview
@Composable
private fun ActiveRunScreenPreview() {
    RuniqueTheme {
        ActiveRunScreen(
            state = ActiveRunState(),
            onAction = {}
        )
    }
}