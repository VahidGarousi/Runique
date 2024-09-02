package ir.runique.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ir.runique.core.database.dao.RunPendingSynDao
import ir.runique.core.domain.run.RemoteRunDataSource

class DeleteRunWorker(
    private val context: Context,
    private val parameters: WorkerParameters,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val pendingSynDao: RunPendingSynDao
) : CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        val runId = parameters.inputData.getString(RUN_ID) ?: return Result.failure()
        return when (val result = remoteRunDataSource.deleteRun(id = runId)) {
            is ir.runique.core.domain.util.Result.Error -> result.error.toWorkerResult()
            is ir.runique.core.domain.util.Result.Success -> {
                pendingSynDao.deleteDeletedRunSyncEntity(runId)
                Result.success()
            }
        }
    }

    companion object {
        const val RUN_ID = "RUN_ID"
    }
}