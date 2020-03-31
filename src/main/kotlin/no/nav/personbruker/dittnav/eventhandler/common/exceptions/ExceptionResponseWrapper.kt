package no.nav.personbruker.dittnav.eventhandler.common.exceptions

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.slf4j.Logger
import java.lang.Exception

suspend fun respondWithError(call: ApplicationCall, log: Logger, exception: Exception) {
    when(exception) {
        is EventCacheException -> {
            call.respond(HttpStatusCode.ServiceUnavailable)
            log.warn("Klarte ikke hente eventer fra cache. Returnerer feilkode til frontend", exception)
        }
        else -> {
            call.respond(HttpStatusCode.InternalServerError)
            log.error("Ukjent feil oppstod ved henting av eventer fra cache. Returnerer feilkode til frontend", exception)
        }
    }
}
