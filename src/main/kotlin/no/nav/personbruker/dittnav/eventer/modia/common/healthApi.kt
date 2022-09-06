package no.nav.personbruker.dittnav.eventer.modia.common

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
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
