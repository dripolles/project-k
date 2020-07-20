package com.github.dripolles.projectk.api

import com.github.dripolles.projectk.auth.AuthConfig
import com.github.dripolles.projectk.session.UserSession
import io.ktor.auth.Authentication
import io.ktor.auth.basic
import io.ktor.sessions.sessions
import io.ktor.sessions.set

/**
 * TestAuthConfig can be used in tests as an alternate implementation of the AuthConfig interface
 *
 * It will always consider the user is authenticaded, and set the session accordingly. Note that
 * even though the user will be the same for every call, the session will be set with a different ID.
 */
class TestAuthConfig(val username: String) : AuthConfig {
    override fun configure(auth: Authentication.Configuration) {
        auth.basic(AuthConfig.USER) {
            skipWhen { call ->
                val userSession = UserSession(username)
                call.sessions.set(userSession)
                true
            }
        }
    }
}
