package com.github.dripolles.projectk.session

import io.ktor.sessions.SessionStorage
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie

class SessionsConfig(
    val userStorage: SessionStorage = SessionStorageMemory()
) {
    companion object {
        private val KEY = "c!:Q4i_bq@.G=bM*>~hUi'XQDUr@n".toByteArray()
    }
    fun configure(sessions: Sessions.Configuration) {
        sessions.cookie<UserSession>("USER", userStorage) {
            cookie.path = "/"
            cookie.extensions["SameSite"] = "lax"
            transform(SessionTransportTransformerMessageAuthentication(KEY))
        }
    }
}
