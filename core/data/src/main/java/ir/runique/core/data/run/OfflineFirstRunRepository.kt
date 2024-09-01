package ir.runique.core.data.run

import ir.runique.core.domain.run.LocalRunDataSource
import ir.runique.core.domain.run.RemoteRunDataSource
import ir.runique.core.domain.run.Run
import ir.runique.core.domain.run.RunId
import ir.runique.core.domain.run.RunRepository
import ir.runique.core.domain.util.DataError
import ir.runique.core.domain.util.EmptyResult
import ir.runique.core.domain.util.Result
import ir.runique.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope
) : RunRepository {
    override fun getRuns(): Flow<List<Run>> = localRunDataSource.getRuns()

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        val localResult = localRunDataSource.upsertRun(run)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }
        val runWithId = run.copy(
            id = localResult.data
        )
        val remoteResult = remoteRunDataSource.postRun(
            run = runWithId,
            mapPicture = mapPicture
        )
        return when (remoteResult) {
            is Result.Error -> {
                Result.Success(Unit)
            }
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun deleteRun(id: RunId) {
        localRunDataSource.deleteRun(id)
        applicationScope.async {
            remoteRunDataSource.deleteRun(id).asEmptyDataResult()
        }.await()
    }
}