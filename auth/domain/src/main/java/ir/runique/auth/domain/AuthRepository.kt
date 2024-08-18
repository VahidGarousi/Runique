package ir.runique.auth.domain

import ir.runique.core.domain.util.DataError
import ir.runique.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Network>

    suspend fun login(
        email: String,
        password: String
    ): EmptyResult<DataError.Network>
}