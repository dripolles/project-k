package com.github.dripolles.projectk.api

import com.github.dripolles.projectk.FeaturesConfig
import com.github.dripolles.projectk.installFeatures
import com.github.dripolles.projectk.session.SessionsConfig
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HomeTest {
    val USER = "testuser"

    private fun withTestProjectK(test: TestApplicationEngine.() -> Unit) {
        val config = FeaturesConfig(
            auth = TestAuthConfig(USER),
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
    fun `home has logged in user`() {
        withTestProjectK {
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
