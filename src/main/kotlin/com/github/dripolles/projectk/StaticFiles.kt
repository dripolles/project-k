package com.github.dripolles.projectk

import io.ktor.application.Application
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.routing.routing

fun Application.staticModule() {
    routing {
        static("/static") {
            files("static")
        }
    }
}
