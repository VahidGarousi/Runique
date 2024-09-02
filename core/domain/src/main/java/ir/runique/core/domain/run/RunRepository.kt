package ir.runique.core.domain.run

import ir.runique.core.domain.util.DataError
import ir.runique.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface RunRepository {
    fun getRuns(): Flow<List<Run>>
    suspend fun fetchRuns(): EmptyResult<DataError>
    suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError>
    suspend fun deleteRun(id: RunId)
    suspend fun syncPendingRuns()

    suspend fun logOut(): EmptyResult<DataError.Network>
    suspend fun deleteAllRuns()
}