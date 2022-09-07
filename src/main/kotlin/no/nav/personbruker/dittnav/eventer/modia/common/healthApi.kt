package no.nav.personbruker.dittnav.eventer.modia.common

import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Route.healthApi(prometheusMeterRegistry: PrometheusMeterRegistry) {

    val pingJsonResponse = """{"ping": "pong"}"""

    get("/isAlive") {
        call.respondText(text = "ALIVE", contentType = ContentType.Text.Plain)
    }

    get("/isReady") {
        call.respondText(text = "READY", contentType = ContentType.Text.Plain)
    }

    get("/ping") {
        call.respondText(pingJsonResponse, ContentType.Application.Json)
    }

    get("/metrics") {
        call.respond(prometheusMeterRegistry.scrape())
    }
}
