package com.github.dripolles.projectk

import com.github.dripolles.projectk.api.homeModule
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HomeTest {
    private fun withTestProjectK(test: TestApplicationEngine.() -> Unit) {
        withTestApplication(
            {
                installFeatures()
                homeModule()
            },
            test
        )
    }

    @Test
    fun root() {
        withTestProjectK {
            handleRequest {
                method = HttpMethod.Get
                uri = "/"
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
