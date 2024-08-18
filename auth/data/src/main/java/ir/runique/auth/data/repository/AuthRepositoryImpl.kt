package ir.runique.auth.data.repository

import io.ktor.client.HttpClient
import ir.runique.auth.data.LoginRequest
import ir.runique.auth.data.LoginResponse
import ir.runique.auth.data.RegisterRequest
import ir.runique.auth.domain.AuthRepository
import ir.runique.core.data.networking.post
import ir.runique.core.domain.AuthInfo
import ir.runique.core.domain.SessionStorage
import ir.runique.core.domain.util.DataError
import ir.runique.core.domain.util.EmptyResult
import ir.runique.core.domain.util.Result
import ir.runique.core.domain.util.asEmptyDataResult

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {
    override suspend fun register(
        email: String,
        password: String
    ): EmptyResult<DataError.Network> {
        return httpClient.post(
            route = "/register",
            body = RegisterRequest(
                email = email,
                password = password
            )
        )
    }

    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        )
        if (result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId = result.data.userId
                )
            )
        }
        return result.asEmptyDataResult()
    }
}