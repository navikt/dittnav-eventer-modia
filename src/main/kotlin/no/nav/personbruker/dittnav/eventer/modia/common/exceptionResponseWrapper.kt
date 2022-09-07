package no.nav.personbruker.dittnav.eventer.modia.common

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import org.slf4j.Logger

suspend fun respondWithError(call: ApplicationCall, log: Logger, exception: Exception) {
    call.respond(HttpStatusCode.InternalServerError)
    log.error("Ukjent feil oppstod ved henting av eventer fra cache. Returnerer feilkode til frontend", exception)
}
