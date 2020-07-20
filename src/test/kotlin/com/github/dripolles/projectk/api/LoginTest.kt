package com.github.dripolles.projectk.api

import com.github.dripolles.projectk.FeaturesConfig
import com.github.dripolles.projectk.auth.FormAuthConfig
import com.github.dripolles.projectk.auth.LoginFormValidator
import com.github.dripolles.projectk.auth.UserIdPrincipal
import com.github.dripolles.projectk.installFeatures
import com.github.dripolles.projectk.session.SessionsConfig
import io.ktor.auth.UserPasswordCredential
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.apache.logging.log4j.kotlin.Logging
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LoginTest {
    companion object : Logging

    // Mock this so we don't break the tests when we write a "real" validator for production
    val loginFormValidator = mockk<LoginFormValidator>()
        .apply {
            val credsSlot = slot<UserPasswordCredential>()
            every { validate(capture(credsSlot)) } answers {
                if (credsSlot.captured == UserPasswordCredential("foo", "bar")) {
                    UserIdPrincipal("foo")
                } else {
                    null
                }
            }

        }

    private fun withTestProjectK(test: TestApplicationEngine.() -> Unit) {
        withTestApplication(
            {
                installFeatures(
                    FeaturesConfig(
                        auth = FormAuthConfig(loginFormValidator),
                        sessions = SessionsConfig()
                    )
                )
                loginModule()
            },
            test
        )
    }

    @Test
    fun `successful login redirects to home`() {
        withTestProjectK {
            handleRequest {
                method = HttpMethod.Post
                uri = Routes.LOGIN
                setBody("username=foo&password=bar")
                addHeader("content-type", "application/x-www-form-urlencoded")
            }.apply {
                assertEquals(HttpStatusCode.Found, response.status())
                assertEquals(Routes.HOME, response.headers["Location"])
            }
        }
    }

    @Test
    fun `unsuccessful login redirects to login page`() {
        withTestProjectK {
            handleRequest {
                method = HttpMethod.Post
                uri = Routes.LOGIN
                setBody("username=foo&password=WRONG")
                addHeader("content-type", "application/x-www-form-urlencoded")
            }.apply {
                assertEquals(HttpStatusCode.Found, response.status())
                assertEquals(Routes.LOGIN, response.headers["Location"])
            }
        }

    }
}
