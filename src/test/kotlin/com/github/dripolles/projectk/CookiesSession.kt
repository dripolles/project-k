package com.github.dripolles.projectk

import io.ktor.http.Cookie
import io.ktor.http.encodeURLParameter
import io.ktor.http.parseServerSetCookieHeader
import io.ktor.server.testing.TestApplicationCall
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.TestApplicationRequest

fun TestApplicationEngine.cookiesSession(
    initialCookies: List<Cookie> = listOf(),
    callback: CookieTrackerTestApplicationEngine.() -> Unit
) {
    callback(CookieTrackerTestApplicationEngine(this, initialCookies))
}

class CookieTrackerTestApplicationEngine(
    val engine: TestApplicationEngine,
    var trackedCookies: List<Cookie> = listOf()
) {

    fun handleRequest(
        closeRequest: Boolean = true,
        setup: TestApplicationRequest.() -> Unit = {}
    ): TestApplicationCall {
        return engine.handleRequest(closeRequest) {
            val cookieValue = trackedCookies.joinToString("; ") {
                it.name.encodeURLParameter() + "=" + it.value.encodeURLParameter()
            }
            addHeader("Cookie", cookieValue)
            setup()
        }.apply {
            val cookiesToSet = response.headers.values("Set-Cookie")
            trackedCookies = cookiesToSet.map { parseServerSetCookieHeader(it) }
        }
    }
}
