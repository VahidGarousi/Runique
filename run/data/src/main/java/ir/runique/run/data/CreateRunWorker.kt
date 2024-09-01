package ir.runique.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ir.runique.core.database.dao.RunPendingSynDao
import ir.runique.core.database.mappers.toRun
import ir.runique.core.domain.run.RemoteRunDataSource

class CreateRunWorker(
    private val context: Context,
    private val parameters: WorkerParameters,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val pendingSynDao: RunPendingSynDao
) : CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        val pendingRunId = parameters.inputData.getString(RUN_ID) ?: return Result.failure()
        val penRunEntity =
            pendingSynDao.getRunPendingSyncEntity(runId = pendingRunId) ?: return Result.failure()
        val run = penRunEntity.run.toRun()
        return when (val result = remoteRunDataSource.postRun(run, penRunEntity.mapPictureBytes)) {
            is ir.runique.core.domain.util.Result.Error -> result.error.toWorkerResult()
            is ir.runique.core.domain.util.Result.Success -> {
                pendingSynDao.deleteRunPendingSyncEntity(pendingRunId)
                Result.success()
            }
        }
    }

    companion object {
        const val RUN_ID = "RUN_ID"
    }
}