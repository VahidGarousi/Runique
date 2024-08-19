package ir.runique.run.presentation.active_run

import ir.runique.core.domain.location.Location
import ir.runique.run.domain.RunData
import kotlin.time.Duration

data class ActiveRunState(
    val elapsedTime: Duration = Duration.ZERO,
    val runData : RunData = RunData(),
    val shouldTrack: Boolean = false,
    val hasStartedRunningAlready: Boolean = false,
    val currentLocation: Location? = null,
    val isRunFinished: Boolean = false,
    val isSavingRun: Boolean = false
)
