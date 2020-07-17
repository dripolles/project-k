package com.github.dripolles.projectk

import com.github.dripolles.projectk.api.homeModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.apache.logging.log4j.kotlin.Logging

object Main : Logging

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        installFeatures()
        homeModule()
    }
    Main.logger.debug { "Starting server" }
    server.start(wait = true)
}

fun Application.installFeatures() {
    install(Routing)
}
