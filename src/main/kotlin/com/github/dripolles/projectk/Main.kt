package com.github.dripolles.projectk

import com.github.dripolles.projectk.api.homeModule
import com.github.dripolles.projectk.api.loginModule
import com.github.dripolles.projectk.auth.AuthConfig
import com.github.dripolles.projectk.auth.FormAuthConfig
import com.github.dripolles.projectk.auth.LoginFormValidator
import com.github.dripolles.projectk.auth.StaticLoginFormValidator
import com.github.dripolles.projectk.session.SessionsConfig
import com.mitchellbosecke.pebble.loader.ClasspathLoader
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.CallLogging
import io.ktor.features.StatusPages
import io.ktor.pebble.Pebble
import io.ktor.routing.Routing
import io.ktor.server.engine.addShutdownHook
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.stop
import io.ktor.server.netty.Netty
import io.ktor.sessions.SessionStorage
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import org.apache.logging.log4j.kotlin.Logging
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import org.slf4j.event.Level
import java.util.concurrent.TimeUnit

object Main : Logging

const val SESSION_STORAGE_TAG = "user"

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        val di = configDependencies()
        val featuresConfig by di.instance<FeaturesConfig>()
        installFeatures(featuresConfig)

        staticModule()
        homeModule()
        loginModule()
    }.apply {
        addShutdownHook {
            stop(gracePeriod = 3, timeout = 5, timeUnit = TimeUnit.SECONDS)
        }
    }

    Main.logger.debug { "Starting server" }
    server.start(wait = true)
}

fun configDependencies(): DI {
    return DI {
        bind<LoginFormValidator>() with singleton { StaticLoginFormValidator() }
        bind<AuthConfig>() with singleton { FormAuthConfig(instance()) }

        bind<SessionStorage>(SESSION_STORAGE_TAG) with singleton { SessionStorageMemory() }
        bind<SessionsConfig>() with singleton { SessionsConfig(instance(SESSION_STORAGE_TAG)) }
        bind<FeaturesConfig>() with singleton { FeaturesConfig(instance(), instance()) }
    }
}

data class FeaturesConfig(
    val auth: AuthConfig,
    val sessions: SessionsConfig
)

fun Application.installFeatures(config: FeaturesConfig) {
    install(Routing)
    install(StatusPages)
    install(CallLogging) { level = Level.INFO }
    install(Sessions) { config.sessions.configure(this) }
    install(Authentication) { config.auth.configure(this) }
    install(Pebble) {
        loader(ClasspathLoader().apply {
          prefix = "templates"
        })
    }
}
