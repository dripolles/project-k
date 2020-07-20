package com.github.dripolles.projectk.session

import io.ktor.sessions.SessionStorage
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie

class SessionsConfig(
    val userStorage: SessionStorage = SessionStorageMemory()
) {
    fun configure(sessions: Sessions.Configuration) {
        sessions.cookie<UserSession>("USER", userStorage) {
            cookie.path = "/"
            cookie.extensions["SameSite"] = "lax"
            transform(SessionTransportTransformerMessageAuthentication("12345".toByteArray()))
        }
    }
}
