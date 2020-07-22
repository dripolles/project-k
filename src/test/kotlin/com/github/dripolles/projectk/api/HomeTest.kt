package com.github.dripolles.projectk.api

import com.github.dripolles.projectk.FeaturesConfig
import com.github.dripolles.projectk.auth.AuthConfig
import com.github.dripolles.projectk.auth.FormAuthConfig
import com.github.dripolles.projectk.auth.LoginFormValidator
import com.github.dripolles.projectk.auth.UserIdPrincipal
import com.github.dripolles.projectk.installFeatures
import com.github.dripolles.projectk.session.SessionsConfig
import io.ktor.auth.UserPasswordCredential
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HomeTest {
    val USER = "testuser"

    private fun withTestProjectK(authConfig: AuthConfig, test: TestApplicationEngine.() -> Unit) {
        val config = FeaturesConfig(
            auth = authConfig,
            sessions = SessionsConfig()
        )
        withTestApplication(
            {
                installFeatures(config)
                homeModule()
            },
            test
        )
    }
    @Test
    fun `unauthenticated home redirects to login`() {
        withTestProjectK(FormAuthConfig(AlwaysFailFormValidator())) {
            handleRequest { method = HttpMethod.Get; uri = Routes.HOME }.apply {
                assertEquals(HttpStatusCode.Found, response.status())
                kotlin.test.assertEquals(Routes.LOGIN, response.headers["Location"])
            }
        }
    }

    @Test
    fun `authenticated home has logged in user`() {
        withTestProjectK(TestAuthConfig(USER)) {
            handleRequest { method = HttpMethod.Get; uri = Routes.HOME }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertTrue(
                    response.content?.contains(USER) ?: false,
                    "Page content should mention the username"
                )
            }
        }
    }
}


class AlwaysFailFormValidator : LoginFormValidator {
    override fun validate(credentials: UserPasswordCredential): UserIdPrincipal? = null
}
