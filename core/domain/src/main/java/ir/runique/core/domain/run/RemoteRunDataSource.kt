package ir.runique.core.domain.run

import ir.runique.core.domain.util.DataError
import ir.runique.core.domain.util.EmptyResult
import ir.runique.core.domain.util.Result

interface RemoteRunDataSource {
    suspend fun getRuns(): Result<List<Run>, DataError.Network>
    suspend fun postRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.Network>
    suspend fun deleteRun(id: String): EmptyResult<DataError.Network>
}