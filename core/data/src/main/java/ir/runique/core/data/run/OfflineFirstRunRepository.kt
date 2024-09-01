package ir.runique.core.data.run

import ir.runique.core.database.dao.RunPendingSynDao
import ir.runique.core.database.mappers.toRun
import ir.runique.core.domain.SessionStorage
import ir.runique.core.domain.run.LocalRunDataSource
import ir.runique.core.domain.run.RemoteRunDataSource
import ir.runique.core.domain.run.Run
import ir.runique.core.domain.run.RunId
import ir.runique.core.domain.run.RunRepository
import ir.runique.core.domain.run.SyncRunScheduler
import ir.runique.core.domain.util.DataError
import ir.runique.core.domain.util.EmptyResult
import ir.runique.core.domain.util.Result
import ir.runique.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope,
    private val runPendingSynDao: RunPendingSynDao,
    private val sessionStorage: SessionStorage,
    private val syncRunScheduler: SyncRunScheduler
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
                applicationScope.launch {
                    syncRunScheduler.scheduleSync(
                        SyncRunScheduler.SyncType.CreateRun(
                            run = runWithId,
                            mapPictureBytes = mapPicture
                        )
                    )
                }.join()
                Result.Success(Unit)
            }

            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun deleteRun(runId: RunId) {
        localRunDataSource.deleteRun(runId)
        // Edge case where the run is created in offline-mode and then deleted in offline-mode
        // In that case we don't need sync anything
        val isPendingSync = runPendingSynDao.getRunPendingSyncEntity(runId = runId) != null
        if (isPendingSync) {
            runPendingSynDao.deleteRunPendingSyncEntity(runId = runId)
            return
        }
        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRun(runId).asEmptyDataResult()
        }.await()

        if (remoteResult is Result.Error) {
            applicationScope.launch {
                syncRunScheduler.scheduleSync(
                    SyncRunScheduler.SyncType.DeletedRun(
                        runId = runId
                    )
                )
            }.join()
        }
    }

    override suspend fun syncPendingRuns() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userId ?: return@withContext
            val createdRuns = async {
                runPendingSynDao.getAllRunPendingSyncEntities(userId = userId)
            }
            val deletedRuns = async {
                runPendingSynDao.getAllDeletedRunSyncEntities(userId = userId)
            }
            val createJobs = createdRuns.await()
                .map {
                    launch {
                        val run = it.run.toRun()
                        when (remoteRunDataSource.postRun(run, it.mapPictureBytes)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSynDao.deleteRunPendingSyncEntity(runId = it.runId)
                                }.join()
                            }
                        }
                    }
                }
            val deleteJobs = deletedRuns.await()
                .map {
                    launch {
                        when (remoteRunDataSource.deleteRun(it.runId)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSynDao.deleteDeletedRunSyncEntity(runId = it.runId)
                                }.join()
                            }
                        }
                    }
                }
            createJobs.joinAll()
            deleteJobs.joinAll()
        }
    }
}