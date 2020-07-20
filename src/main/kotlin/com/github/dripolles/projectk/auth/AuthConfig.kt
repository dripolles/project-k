package com.github.dripolles.projectk.auth

import com.github.dripolles.projectk.api.Routes
import com.github.dripolles.projectk.session.UserSession
import io.ktor.auth.Authentication
import io.ktor.auth.Principal
import io.ktor.auth.UserPasswordCredential
import io.ktor.auth.form
import io.ktor.sessions.get
import io.ktor.sessions.sessions

interface AuthConfig {
    companion object {
        const val USER = "user"
    }

    fun configure(auth: Authentication.Configuration)
}

class FormAuthConfig(val loginFormValidator: LoginFormValidator) : AuthConfig {

    override fun configure(auth: Authentication.Configuration) {
        auth.form(name = AuthConfig.USER) {
            skipWhen { call -> call.sessions.get<UserSession>() != null }
            userParamName = "username"
            passwordParamName = "password"
            challenge(Routes.LOGIN)
            validate { credentials -> loginFormValidator.validate(credentials) }
        }
    }
}

interface LoginFormValidator {
    fun validate(credentials: UserPasswordCredential): UserIdPrincipal?
}

data class UserIdPrincipal(val username: String) : Principal
