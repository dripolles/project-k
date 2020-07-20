package com.github.dripolles.projectk.auth

import io.ktor.auth.UserPasswordCredential
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

// This is a very straightforward class, but let's add tests just in case
class StaticLoginFormValidatorTest {
    val validator: LoginFormValidator = StaticLoginFormValidator()

    companion object {
        @JvmStatic
        fun goodCredentials(): List<UserPasswordCredential> {
            return StaticLoginFormValidator.users.map { (username, password) ->
                UserPasswordCredential(username, password)
            }
        }

        @JvmStatic
        fun badCredentials(): List<UserPasswordCredential> {
            return listOf(
                UserPasswordCredential("wronguser", "wrongpassword"),
                UserPasswordCredential(
                    StaticLoginFormValidator.users.keys.first(),
                    StaticLoginFormValidator.users.values.first() + "makeitwromg"
                )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("goodCredentials")
    fun `users in list should succeed validation`(credential: UserPasswordCredential) {
        val expected = UserIdPrincipal(credential.name)
        assertEquals(
            expected,
            validator.validate(credential),
            "User should be validated"
        )
    }

    @ParameterizedTest
    @MethodSource("badCredentials")
    fun `wrong user fails validation`(credential: UserPasswordCredential) {
        assertNull(validator.validate(credential))
    }
}
