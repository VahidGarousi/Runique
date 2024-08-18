package ir.runique.core.data.auth

import ir.runique.core.domain.AuthInfo

fun AuthInfo.mapToAuthInfoSerializable(): AuthInfoSerializable = AuthInfoSerializable(
    accessToken = accessToken,
    refreshToken = refreshToken,
    userId = userId
)


fun AuthInfoSerializable.mapToAuthInfo(): AuthInfo = AuthInfo(
    accessToken = accessToken,
    refreshToken = refreshToken,
    userId = userId
)
