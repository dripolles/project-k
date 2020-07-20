package com.github.dripolles.projectk.api

import com.github.dripolles.projectk.auth.AuthConfig
import com.github.dripolles.projectk.session.UserSession
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.sessions.get
import io.ktor.sessions.sessions

fun Application.homeModule() {
    routing {
        authenticate(AuthConfig.USER) {
            get(Routes.HOME) {
                val userSession = call.sessions.get<UserSession>()!!
                call.respond("Home of user: ${userSession.username}")
            }
        }
    }
}
