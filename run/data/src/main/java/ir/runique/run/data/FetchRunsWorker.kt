package ir.runique.run.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ir.runique.core.domain.run.RunRepository
import ir.runique.core.domain.util.DataError
import ir.runique.core.domain.util.Result

class FetchRunsWorker(
    private val context: Context,
    private val params: WorkerParameters,
    private val runRepository: RunRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        return when (val result = runRepository.fetchRuns()) {
            is ir.runique.core.domain.util.Result.Error ->  result.error.toWorkerResult()

            is ir.runique.core.domain.util.Result.Success -> Result.success()
        }
    }


}