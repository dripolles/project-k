package com.github.dripolles.projectk.api

import com.github.dripolles.projectk.auth.AuthConfig
import com.github.dripolles.projectk.auth.UserIdPrincipal
import com.github.dripolles.projectk.session.UserSession
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.sessions.sessions
import io.ktor.sessions.set

fun Application.loginModule() {
    routing {
        get(Routes.LOGIN) {
            call.respondText("Placeholder for the login form")
        }
        authenticate(AuthConfig.USER) {
            post(Routes.LOGIN) {
                val principal = call.principal<UserIdPrincipal>()!!
                call.sessions.set(UserSession(principal.username))
                call.respondRedirect(Routes.HOME)
            }
        }
    }
}
