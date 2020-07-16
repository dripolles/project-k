package com.github.dripolles.projectk.api

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.homeModule() {
    routing {
        get("/") {
            call.respond("Hello world")
        }
    }
}