package ir.runique.auth.data.repository

import io.ktor.client.HttpClient
import ir.runique.auth.data.RegisterRequest
import ir.runique.auth.domain.AuthRepository
import ir.runique.core.data.networking.post
import ir.runique.core.domain.util.DataError
import ir.runique.core.domain.util.EmptyResult

class AuthRepositoryImpl(
    private val httpClient: HttpClient
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
}