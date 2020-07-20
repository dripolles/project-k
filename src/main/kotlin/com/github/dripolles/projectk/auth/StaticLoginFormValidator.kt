package com.github.dripolles.projectk.auth

import io.ktor.auth.UserPasswordCredential

class StaticLoginFormValidator : LoginFormValidator {
    companion object {
        val users = mapOf(
            "user1" to "password1",
            "user2" to "password2"
        )
    }

    override fun validate(credentials: UserPasswordCredential): UserIdPrincipal? {
        return users[credentials.name]?.let { password ->
            if (credentials.password == password) {
                UserIdPrincipal(credentials.name)
            } else {
                null
            }
        }
    }
}
