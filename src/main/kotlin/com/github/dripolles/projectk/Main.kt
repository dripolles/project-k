package com.github.dripolles.projectk

import com.github.dripolles.projectk.api.homeModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty


fun main(args: Array<String>) {
    println("Hello world")
    val server = embeddedServer(Netty, port = 8080) {
        installFeatures()
        homeModule()
    }
    server.start(wait = true)
}

fun Application.installFeatures() {
    install(Routing)
}
