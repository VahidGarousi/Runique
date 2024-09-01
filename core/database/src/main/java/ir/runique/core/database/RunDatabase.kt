package ir.runique.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.runique.core.database.dao.RunDao
import ir.runique.core.database.dao.RunPendingSynDao
import ir.runique.core.database.entity.DeletedRunSyncEntity
import ir.runique.core.database.entity.RunEntity
import ir.runique.core.database.entity.RunPendingSyncEntity

@Database(
    entities = [
        RunEntity::class,
        RunPendingSyncEntity::class,
        DeletedRunSyncEntity::class
    ],
    version = 1
)
abstract class RunDatabase : RoomDatabase() {

    abstract val runDao: RunDao
    abstract val runPendingSynDao: RunPendingSynDao
}